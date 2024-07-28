package com.overdevx.mystoryapp.ui.home

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.overdevx.mystoryapp.DataDummy
import com.overdevx.mystoryapp.MainDispatcherRule
import com.overdevx.mystoryapp.data.adapter.StoryAdapter
import com.overdevx.mystoryapp.data.database.ListStoryItemRoom
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        logMock = Mockito.mockStatic(Log::class.java)
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryItemRoom> = StoryPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<ListStoryItemRoom>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getListStory()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<ListStoryItemRoom> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItemRoom> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<ListStoryItemRoom>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getListStory()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<ListStoryItemRoom> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }

    @After
    fun tearDown() {
        logMock.close()
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItemRoom>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItemRoom>): PagingData<ListStoryItemRoom> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItemRoom>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItemRoom>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


