package com.swp.VinGiG.utilities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class Constants {
	public static final int DEFAULT_BADGEID = 1;
	public static final int NEW_PROVIDER_BADGEID = 6;
	public static final int MONTH_PERIOD = 30;
	public static final String DEFAULT_AVATAR = "https://media.istockphoto.com/id/1311497219/vector/stylish-avatar-in-black-in-a-yellow-circle-profile-template-for-websites-applications-social.jpg?s=612x612&w=0&k=20&c=SzDRaxQ_ckdqqPbjFqw23uTgj6bbu3x4G66O7j08o6U=";
	public static final int REVIEW_MIN = 1;
	public static final int REVIEW_MAX = 5;
	public static final int USERNAME_MIN = 6;
	public static final int PASSWORD_MIN = 6;
	public static final int COUNT_DAY_LEFT_NOTI = 5;
	public static final int COUNT_DAY_OVERDUE = -3;
	public static final long WALLET_BALANCE_THRESHOLD = 5000;
	public static final Date START_DATE = new Date(05/05/2023);
	public static final Date currentDate() {
		return new Date();
	}
	
	public static final Date subtractDaysFromDate(Date date, int days) {
		LocalDate currenDate = LocalDate.now();
		currenDate.minusDays(days);
		return Date.from(currenDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static final long DEFAULT_LOWER = -10000;
	public static final long DEFAULT_UPPER = 1000000000;
	public static final long NEW_WALLET_BALANCE_PROMO = 10000;
}
