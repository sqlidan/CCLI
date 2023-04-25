package com.haiersoft.ccli.system.utils;

public class Test {
	// 实现公平锁，依次交替打印26字母
	// public static ReentrantLock lock = new ReentrantLock(true);
	// 共计26字母
	public static String[] words = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
			"q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

	public static volatile int wordIndex = 0;

	private static boolean isYuanWord(String word) {
		switch (word) {
		case "a":
			return true;
		case "e":
			return true;
		case "i":
			return true;
		case "o":
			return true;
		case "u":
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static class TA implements Runnable {

		@Override
		public void run() {
			// wordIndex = 0;
			StringBuffer sb = new StringBuffer();
			sb.append("我是线程TA:输出的元音字母为：");
			// sb.append(wordIndex);
			while (wordIndex < words.length) {
				for (; wordIndex < words.length; wordIndex++) {
					if (isYuanWord(words[wordIndex])) {
						sb.append(words[wordIndex]);
					} else {
						break;
					}
				}
			}
			System.out.println(sb.toString());
			/*
			 * while (wordIndex < words.length) { try { Thread.sleep(1000); }
			 * catch (Exception e) {
			 * 
			 * } //先加锁
			 * 
			 * //lock.lock(); System.out.println("我是线程TA:抢夺上锁");
			 * //执行线程TA只输出元音字母，其他字母则跳出循环 //循环数组 try { for (; wordIndex <
			 * words.length; wordIndex++) { if (isYuanWord(words[wordIndex])) {
			 * System.out.println("我是线程TA:输出的元音字母为：" + words[wordIndex]); } else
			 * { break; } } } catch (Exception e) {
			 * 
			 * } finally { //lock.unlock(); System.out.println("我是线程TA:释放锁"); }
			 * }
			 */
		}
	}

	@SuppressWarnings("unused")
	private static class TB implements Runnable {

		@Override
		public void run() {
			StringBuffer sb = new StringBuffer();
			sb.append("我是线程TB:输出的辅音字母为：");
			// wordIndex = 0;
			// sb.append(wordIndex);

			while (wordIndex < words.length) {
				for (; wordIndex < words.length; wordIndex++) {
					if (!isYuanWord(words[wordIndex])) {
						sb.append(words[wordIndex]);
					} else {
						break;
					}
				}
			}
			System.out.println(sb.toString());
			/*
			 * wordIndex = 0; while (wordIndex < words.length) { try {
			 * Thread.sleep(1000); } catch (Exception e) {
			 * 
			 * } //先加锁 //lock.lock(); System.out.println("我是线程TB:抢夺上锁");
			 * //执行线程TA只输出元音字母，其他字母则跳出循环 //循环数组 try { for (; wordIndex <
			 * words.length; wordIndex++) { if (!isYuanWord(words[wordIndex])) {
			 * System.out.println("我是线程TB:输出的辅音字母为：" + words[wordIndex]); } else
			 * { break; } } } catch (Exception e) {
			 * 
			 * } finally { //lock.unlock(); System.out.println("我是线程TB:释放锁"); }
			 * }
			 */
		}
	}

	@SuppressWarnings("unused")
	private static int f(int n) {
		if (n <= 2)
			return n;

		int f1 = 1;
		int f2 = 2;
		int sum = 0;

		for (int i = 3; i <= n; i++) {
			sum = f1 + f2;
			System.out.println(sum + "=" + f1 + "+" + f2);
			f1 = f2;
			f2 = sum;
		}
		return sum;
	}

	public static void main(String[] args) {

		if (8 >= 6 && 8 < 11) {
			System.out.println("cddd");

		}
		// System.out.println(f(7));
		// 声明两个线程，如果是元音字母则A线程输出，如果是辅音字母则B线程输出
		/*
		 * TA a = new TA(); Thread tA = new Thread(a); TB b = new TB(); Thread
		 * tB = new Thread(b);
		 */
		// 启动的时候抢夺CPU时间分片资源不一定谁先抢到，所以设定初始字母判断
		/*
		 * if (words.length <= 0) { System.out.println("请完善数组，数组不能问为空！");
		 * return; } tA.start(); tB.start();
		 */
	}

}
