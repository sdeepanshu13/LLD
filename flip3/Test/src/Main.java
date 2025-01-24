//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        MovieBookingSystem system = new MovieBookingSystem();

        Thread cust1 = new Thread(() -> {
            if (system.holdSeat("cust1")) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                //system.bookSeat("cust1");
                system.releaseSeat("cust1");
            }
        });

        Thread cust2 = new Thread(() -> {
            if (system.holdSeat("cust2")) {
                if(!system.bookSeat("cust2")) {
                    system.releaseSeat("cust2");
                }
            }


        });

        cust1.start();
        Thread.sleep(2000);
        cust2.start();

        cust1.join();
        cust2.join();

        if(system.holdSeat("cust2")){
            system.bookSeat("cust2");
        }
    }
}