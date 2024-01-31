package com.nero.dronetask.configs;

public class Routes {

    public static class Exception {
		public static final String DEFAULT_ERROR = "/error";
	}

	public static class Drone {
		public static final String INDEX = "drones";
		public static final String SHOW = "drones/{id}";

		public static class Operation {
			public static final String LOAD = "drones/{id}/load";
			public static final String CHARGE = "drones/{id}/charge";
			public static final String STATE = "drones/{id}/state";
		}

	}

	public static class Medication {
		public static final String INDEX = "medications";
	}
}