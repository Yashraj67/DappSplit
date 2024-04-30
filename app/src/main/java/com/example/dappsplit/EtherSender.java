package com.example.dappsplit;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class EtherSender extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b506103698061001c5f395ff3fe60806040526004361061001d575f3560e01c806348c981e214610021575b5f80fd5b61003b600480360381019061003691906101f7565b61003d565b005b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036100ab576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016100a29061027c565b60405180910390fd5b5f34116100ed576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016100e4906102e4565b60405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166108fc3490811502906040515f60405180830381858888f19350505050158015610130573d5f803e3d5ffd5b508073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fe542f7eace00597675a71f483aa6d1634c3833c9e7afd7d0089b291f5e04fdb63460405161018e919061031a565b60405180910390a350565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6101c68261019d565b9050919050565b6101d6816101bc565b81146101e0575f80fd5b50565b5f813590506101f1816101cd565b92915050565b5f6020828403121561020c5761020b610199565b5b5f610219848285016101e3565b91505092915050565b5f82825260208201905092915050565b7f496e76616c6964207265636569766572206164647265737300000000000000005f82015250565b5f610266601883610222565b915061027182610232565b602082019050919050565b5f6020820190508181035f8301526102938161025a565b9050919050565b7f416d6f756e74206d7573742062652067726561746572207468616e20300000005f82015250565b5f6102ce601d83610222565b91506102d98261029a565b602082019050919050565b5f6020820190508181035f8301526102fb816102c2565b9050919050565b5f819050919050565b61031481610302565b82525050565b5f60208201905061032d5f83018461030b565b9291505056fea2646970667358221220dd9080ad14a924dae444089c8f67a13fd28d31db9665432b44c16f134ad5917764736f6c63430008190033";

    private static String librariesLinkedBinary;

    public static final String FUNC_SENDETHER = "sendEther";

    public static final Event ETHERTRANSFERRED_EVENT = new Event("EtherTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected EtherSender(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EtherSender(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EtherSender(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EtherSender(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }



    public static EtherTransferredEventResponse getEtherTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ETHERTRANSFERRED_EVENT, log);
        EtherTransferredEventResponse typedResponse = new EtherTransferredEventResponse();
        typedResponse.log = log;
        typedResponse._sender = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse._receiver = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<EtherTransferredEventResponse> etherTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEtherTransferredEventFromLog(log));
    }

    public Flowable<EtherTransferredEventResponse> etherTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ETHERTRANSFERRED_EVENT));
        return etherTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> sendEther(String _receiver, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SENDETHER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _receiver)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    @Deprecated
    public static EtherSender load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EtherSender(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EtherSender load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EtherSender(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EtherSender load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new EtherSender(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EtherSender load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EtherSender(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EtherSender> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EtherSender.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EtherSender> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EtherSender.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<EtherSender> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EtherSender.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EtherSender> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EtherSender.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }


    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class EtherTransferredEventResponse extends BaseEventResponse {
        public String _sender;

        public String _receiver;

        public BigInteger _amount;
    }
}
