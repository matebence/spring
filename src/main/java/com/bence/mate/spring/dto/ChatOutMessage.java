package com.bence.mate.spring.dto;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatOutMessage {

	@Getter
    private final String content;
	
	@Getter
	@Setter
    private String groupName;
	
	@Getter
	@Setter
    private Date sentTimestamp;

	@Override
	public String toString() {
		return "ChatOutMessage [content=" + content + ", groupName=" + groupName + ", sentTimestamp=" + sentTimestamp
				+ "]";
	}
}
