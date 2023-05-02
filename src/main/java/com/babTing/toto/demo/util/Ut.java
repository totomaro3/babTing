package com.babTing.toto.demo.util;

public class Ut {

	public static boolean empty(Object obj) {

		if (obj == null) {
			return true;
		}

		if (obj instanceof String == false) {
			return true;
		}

		String str = (String) obj;

		return str.trim().length() == 0;
	}

	public static String f(String format, Object... args) {
		return String.format(format, args);
	}

	public static String jsHistoryBack(String resultCode, String msg) {
		return Ut.f("""
				<script>
					alert("%s (code: %s)");
					history.back();
				</script>
					""", msg, resultCode);
	}

	public static String jsReplace(String resultCode, String msg, String uri) {
		return Ut.f("""
				<script>
					alert("%s (code: %s)");
					location.replace('%s');
				</script>
					""", msg, resultCode, uri);
	}

}