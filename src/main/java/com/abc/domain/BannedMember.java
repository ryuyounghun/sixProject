package com.abc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannedMember {
	private String memberId; 
    private String message;
    private String reporter;
    private String inputdate;
}
