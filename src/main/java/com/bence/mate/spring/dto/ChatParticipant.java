package com.bence.mate.spring.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipant {

	@Getter
	@Setter
    private String firstName;
	
	@Getter
	@Setter
    private String lastName;
	
	@Getter
	@Setter
    private String shortName;
	
	@Getter
	@Setter
    private String participantType;

	@Override
	public String toString() {
		return "ChatParticipant [firstName=" + firstName + ", lastName=" + lastName + ", shortName=" + shortName
				+ ", participantType=" + participantType + "]";
	}
}
