package examples.exception;

public class ChainingExceptionExample {
	
	public static void main(String[] args) {

		System.out.println("***no chaining example:");
		try {
			try {
				throw new Exception("One");
			} catch (Exception e) {
				throw new Exception("Two");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		System.out.println("\n***chaining example 1:");
		try {
			try {
				throw new Exception("Three");
			} catch (Exception e) {
				throw new Exception("Four", e);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("###what was the cause:");
			e.getCause().printStackTrace(System.out);
		}

		System.out.println("\n***chaining example 2:");
		try {
			try {
				throw new Exception("Five");
			} catch (Exception e) {
				throw new Exception(e);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
}