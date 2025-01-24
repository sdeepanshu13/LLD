import java.util.concurrent.atomic.AtomicBoolean;

public class MovieBookingSystem {

    private final AtomicBoolean seatAvailable = new AtomicBoolean(true);
    private String currentCustomerHolding = null;

    public synchronized boolean holdSeat(String cutomerName){
        if(!seatAvailable.get()){
            System.out.println(cutomerName+ ": Seats not available ");
            return false;
        }


        if(currentCustomerHolding == null){
            currentCustomerHolding = cutomerName;
            System.out.println("Seat on hold by "+cutomerName);
            return true;
        }else{
            System.out.println("No seats present");
            return false;
        }
    }

    public synchronized  boolean bookSeat(String customerName){

        if(!seatAvailable.get()){
            System.out.println("Seat already booked by: "+ customerName);
            return false;
        }
        if(customerName.equals(currentCustomerHolding)){
            seatAvailable.set(false);
            currentCustomerHolding=null;
            System.out.println("Seat booking successful: "+ customerName);
            return true;
        }else {
            System.out.println("seat not available");
            return false;
        }
    }

    public synchronized  void releaseSeat(String customerName){


        if(customerName.equals(currentCustomerHolding)){
            System.out.println("Seat not booked. Releaseing it");
            currentCustomerHolding= null;
        }else{
            System.out.println("No seat held");
        }
    }
}
