package com.barlow.client.knal.opendata.api.response.item;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record CommitPetitionListItem(
        @JacksonXmlProperty(localName = "committeecode") String committeeCode,
        @JacksonXmlProperty(localName = "committeename") String committeeName
) {
}
