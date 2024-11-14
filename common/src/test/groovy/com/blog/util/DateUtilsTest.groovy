package com.blog.util

import spock.lang.Specification


class DateUtilsTest extends Specification {

    DateUtils dateUtils = new DateUtils()

    def "ParseCustomDate"() {
        given:
        def s = "Fri, 15 Nov 2024 02:48:05 +0900"
        when:
        def date = dateUtils.parseDateWithTimeZone(s)

        then:
        print "${date}"
    }
}
