package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class NeedsVerificationException extends BusinessException {
	private final String invitationLink;

	public NeedsVerificationException(ErrorCode errorCode, String invitationLink) {
		super(errorCode);
		this.invitationLink = invitationLink;
	}

	public NeedsVerificationException(ErrorCode errorCode, String message, String invitationLink) {
		super(errorCode, message);
		this.invitationLink = invitationLink;
	}

	public NeedsVerificationException(ErrorCode errorCode, String message, Throwable cause, String invitationLink) {
		super(errorCode, message, cause);
		this.invitationLink = invitationLink;
	}

}