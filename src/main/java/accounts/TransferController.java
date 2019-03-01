package accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransferController {
    private static Logger log = LoggerFactory.getLogger(TransferController.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public TransferController() {
        //returns balance of account if exists
        get("/balance/:accountId", (request, response) -> {
            int accountId = Integer.parseInt(request.params(":accountId"));
            return Calculator.INSTANCE.getAccountBalance(accountId);
        });

        //creates new account with specified balance
        post("/account", (request, response) -> {
            Account newAccount = objectMapper.readValue(request.body(), Account.class);
            if (Calculator.INSTANCE.addAccount(newAccount.getId(), newAccount.getBalance())) {
                return "OK";
            } else {
                return "Account already exists";
            }
        });

        //transfer money from one account to another
        post("/transfer", (request, response) -> {
            TransferDTO transferDTO = objectMapper.readValue(request.body(), TransferDTO.class);
            Calculator.INSTANCE.transfer(transferDTO.getSourceId(), transferDTO.getTargetId(), transferDTO.getAmount());
            return "OK";
        });

        //Handles all exceptions
        Spark.exception(Exception.class, (exception, request, response) -> {
            log.error(">", exception);
        });
    }
}
