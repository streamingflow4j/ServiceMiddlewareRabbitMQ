package com.service.middleware.cep.subscribe;

/**
 * Class interface to let us easily contain the CEP statements with the Subscribers -
 * just for clarity so it's easy to see the statements the subscribers are registered against.
 */
public interface StatementSubscriber {

    /**
     * Get the EPL Stamement the Subscriber will listen to.
     * @return EPL Statement
     */
    public String getStatement();

    public void setStatement(String statement);

}
