package si.um.feri.grpc.calculator.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import si.um.feri.calculator.CalculatorServiceGrpc;
import si.um.feri.calculator.PrimeNumberDecompositionRequest;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        //Unary
        /*
        SumRequest request = SumRequest.newBuilder().setFirstNumber(10).setSecondNumber(25).build();
        SumResponse response = stub.sum(request);
        System.out.printf(
                "%s + %s = %s%n", request.getFirstNumber(),
                request.getSecondNumber(),
                response.getSumResult());
        */

        //Streaming server
        var number = 567890304;

        stub.primeNumberDecomposition(
                        PrimeNumberDecompositionRequest
                                .newBuilder()
                                .setNumber(number)
                                .build()
                )
                .forEachRemaining(primeNumberDecompositionResponse ->
                        System.out.println(primeNumberDecompositionResponse.getPrimeFactor())
                );

        channel.shutdown();
    }
}
