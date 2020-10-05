package cenk.sy.backend.util;

import java.time.LocalDate;

import cenk.sy.jpa.entity.player.SyPlayer;

public class SyUtil {
	public static int getAge(SyPlayer player, int year) {
		// rule of thumb
		return year - player.getBirthYear();
	}

	public static int getYear() {
		return LocalDate.now().getYear();
	}
}
