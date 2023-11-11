import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession; // Tambahkan import ini
import java.util.ArrayList;
import tracker.ejb.TrackerBean;

@WebServlet(name = "Tracker", urlPatterns = {"/Tracker"})
public class Tracker extends HttpServlet {
    TrackerBean trackerBean = new TrackerBean();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            double total;
            double average = 0;
            int count = 0;
            HttpSession session = request.getSession(); 

            ArrayList<Double> numbers = (ArrayList<Double>) session.getAttribute("numbers");
            if (numbers == null) {
                numbers = new ArrayList<>();
            }

            ArrayList<Double> evenNumbers = (ArrayList<Double>) session.getAttribute("evenNumbers");
            if (evenNumbers == null) {
                evenNumbers = new ArrayList<>();
            }

            ArrayList<Double> oddNumbers = (ArrayList<Double>) session.getAttribute("oddNumbers");
            if (oddNumbers == null) {
                oddNumbers = new ArrayList<>();
            }

            if (!request.getParameter("value").isEmpty()) {
                double inputValue = Double.parseDouble(request.getParameter("value"));
                total = trackerBean.add(inputValue);
                numbers.add(inputValue);

                if (inputValue % 2 == 0) {
                    evenNumbers.add(inputValue);
                } else {
                    oddNumbers.add(inputValue);
                }
            } else {
                total = trackerBean.getTotal();
            }

            if (trackerBean.getCount() != 0) {
                average = trackerBean.average();
                count = trackerBean.getCount();
            }

            out.println("Count: " + count + "<br />");
            out.println("Total: " + total + "<br />");
            out.println("Average: " + average + "<br />");

            out.println("All Numbers: " + numbers.toString() + "<br />");
            out.println("Even Numbers: " + evenNumbers.toString() + "<br />");
            out.println("Odd Numbers: " + oddNumbers.toString() + "<br />");

            session.setAttribute("numbers", numbers);
            session.setAttribute("evenNumbers", evenNumbers);
            session.setAttribute("oddNumbers", oddNumbers);

            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
            rd.include(request, response);
        } catch (IOException | NumberFormatException | ServletException ex) {
            PrintWriter out = response.getWriter();
            out.println("Error: Silahkan isi field dengan angka");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
            rd.include(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
