import java.util.Scanner;
public class SQLiteExample {
    public static void main(String[] args) {
        System.out.println("Welcome to Samanji Online Banking ");
        System.out.println("Press 1 to login \n Press 2 to Register \n Press 3 to Exit");
        Scanner s = new Scanner(System.in);
        int choice = s.nextInt();
        while (true) {
            if (choice == 1) {
                Customer c1 = new Customer();
                c1.login();
                break;
            } else if (choice == 2) {
                Customer c1 = new Customer();
                c1.registrationNetBanking();
                while (true) {
                    System.out.println("Press 1 to Login \nPress 2 to Exit");
                    choice = s.nextInt();
                    if (choice == 1) {
                        c1.login();
                    } else if (choice == 2) {
                        System.out.println("See You Again");
                        return;
                    } else {
                        System.out.println("Please Press 1 or 2 ");
                    }
                }
            } else if (choice == 3) {
                System.out.println("See You Again");
                return;
            }
            System.out.println("Finished Main Class ");
        }
    }
}
