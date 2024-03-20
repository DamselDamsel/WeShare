package weshare.server;

import weshare.controller.*;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {
    public static final String LOGIN_PAGE = "/";
    public static final String LOGIN_ACTION = "/login.action";
    public static final String LOGOUT = "/logout";
    public static final String EXPENSES = "/expenses";
    public static final String NEW_EXPENSES = "/newexpense";
    public static final String EXPENSES_ACTION = "/expense.action";
    public static final String PAYMENT_REQUESTS_SENT = "/paymentrequests_sent";
    public static final String PAYMENT_REQUESTS_RECEIVED = "/paymentrequests_received";
    public static final String PAYMENT_REQUEST = "/paymentrequest";

    public static final String PAYMENT_ACTION = "/payment.action";

    public static void configure(WeShareServer server) {
        server.routes(() -> {
            post(LOGIN_ACTION,             PersonController.login);
            post(EXPENSES_ACTION,          ExpensesController.addExpense);
            post(PAYMENT_REQUEST,          PaymentRequestsController.addPaymentRequest);
            get(LOGOUT,                    PersonController.logout);
            get(EXPENSES,                  ExpensesController.view);
            get(NEW_EXPENSES ,             ExpensesController.new_expense);
            get(PAYMENT_REQUESTS_SENT,     PaymentRequestsController.paymentsent);
            get(PAYMENT_REQUESTS_RECEIVED, PaymentRequestsController.paymentreceived);
            get(PAYMENT_REQUEST,           PaymentRequestsController.submitPaymentRequest);
            post(PAYMENT_ACTION, PaymentRequestsController.paymentAction);
            
        });
    }

}
