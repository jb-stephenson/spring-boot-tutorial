package com.springbootdemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.springbootdemo.model.SiteUser;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SiteUser>{

	@Override
	public void initialize(PasswordMatch arg0) {
		
	}

	@Override
	public boolean isValid(SiteUser user, ConstraintValidatorContext c) {
		String plainPassword = user.getPlainPassword();
		String matchPassword = user.getRepeatPassword();
		
		//This validation did not come from the register form, so it should pass
		if((plainPassword == null) || (plainPassword.length() == 0)) {
			return true;
		}
		
		//This validation did come from the register form
		if((plainPassword == null) || (!plainPassword.contentEquals(matchPassword))) {
			return false;
		}
		
		return true;
	}

}
