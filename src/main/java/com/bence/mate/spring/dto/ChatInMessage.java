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
public class ChatInMessage {

	@Getter
	@Setter
    private String senderId;
	@Getter
	@Setter
    private String senderName;
    
	@Getter
	private final String message;
    
	@Getter
	@Setter
	private Date sentTimestamp;

	@Override
	public String toString() {
		return "ChatInMessage [senderId=" + senderId + ", senderName=" + senderName + ", message=" + message
				+ ", sentTimestamp=" + sentTimestamp + "]";
	}
}
