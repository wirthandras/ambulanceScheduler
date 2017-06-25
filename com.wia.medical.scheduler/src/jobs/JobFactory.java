package jobs;

import java.util.HashSet;
import java.util.Set;

public class JobFactory {

	public static Doctor doctor = new Doctor();
	public static Mentotiszt mento = new Mentotiszt();
	public static Apolo apolo = new Apolo();
	public static Szakapolo szakapolo = new Szakapolo();
	public static Driver driver = new Driver();

	public static IJob getJob(String jobName) {

		if (jobName == null) {
			return null;
		}

		if (jobName.equalsIgnoreCase("MENTO_TISZT")) {
			return mento;
		} else if (jobName.equalsIgnoreCase("APOLO")) {
			return apolo;
		} else if (jobName.equalsIgnoreCase("SZAKAPOLO")) {
			return szakapolo;
		} else if (jobName.equalsIgnoreCase("DRIVER")) {
			return driver;
		} else if (jobName.equalsIgnoreCase("DOCTOR")) {
			return doctor;
		}

		return null;
	}

	public static Set<IJob> getAll() {
		Set<IJob> jobs = new HashSet<IJob>();
		jobs.add(mento);
		jobs.add(apolo);
		jobs.add(driver);
		return jobs;
	}

}
