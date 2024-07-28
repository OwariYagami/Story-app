package com.overdevx.mystoryapp

import com.overdevx.mystoryapp.data.database.ListStoryItemRoom

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItemRoom> {
        val items: MutableList<ListStoryItemRoom> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItemRoom(
               "https://th.bing.com/th/id/OIP.twELSMziQMU5TCzXJUOfQgAAAA?rs=1&pid=ImgDetMain",
                "2022-01-08T06:34:18.598Z",
                "yagami $i",
                "desc $i",
                -16.002,
                i.toString(),
                -10.212
            )
            items.add(story)
        }
        return items
    }
}