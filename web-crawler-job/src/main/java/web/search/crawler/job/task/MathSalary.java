package web.search.crawler.job.task;

public class MathSalary {


    /**
     * Get salary range
     *
     * @param salaryStr
     * @return
     */
    public static Integer[] getSalary(String salaryStr) {
        Integer[] salary = new Integer[2];

        String date = salaryStr.substring(salaryStr.length() - 1, salaryStr.length());
        // If it is calculated by day, multiply it by 240 directly
        if (!"月".equals(date) && !"年".equals(date)) {
            salaryStr = salaryStr.substring(0, salaryStr.length() - 2);
            salary[0] = salary[1] = str2Num(salaryStr, 240);
            return salary;
        }

        String unit = salaryStr.substring(salaryStr.length() - 3, salaryStr.length() - 2);
        String[] salarys = salaryStr.substring(0, salaryStr.length() - 3).split("-");


        salary[0] = mathSalary(date, unit, salarys[0]);
        salary[1] = mathSalary(date, unit, salarys[1]);

        return salary;
    }

    // Calculate salary according to conditions
    private static Integer mathSalary(String date, String unit, String salaryStr) {
        Integer salary = 0;

        // Judge whether the unit is 10000
        if ("万".equals(unit)) {
            // If it is 10000, multiply the salary by 10000
            salary = str2Num(salaryStr, 10000);
        } else {
            // Otherwise multiply by 1000
            salary = str2Num(salaryStr, 1000);
        }

        // Judge whether the time is month
        if ("月".equals(date)) {
            // If it is a month, multiply the salary by 12
            salary = str2Num(salary.toString(), 12);
        }

        return salary;
    }


    private static int str2Num(String salaryStr, int num) {
        try {
            // To convert a string to a decimal, you must accept it with number,
            // otherwise there will be a loss of precision
            Number result = Float.parseFloat(salaryStr) * num;
            return result.intValue();
        } catch (Exception e) {
        }
        return 0;
    }
}
