package si.um.feri.grpc.greeting.server;

import io.grpc.stub.StreamObserver;
import si.um.feri.greet.*;

import java.util.stream.IntStream;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        //create the response
        String result = "Hello %s".formatted(firstName);
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        IntStream.range(1, 101).forEach(i -> {
            String result = "Hello %s, response number: %s".formatted(firstName, i);
            GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<>() {

            String result = "";

            @Override
            public void onNext(LongGreetRequest value) {
                //client send a message
                result += "Hello " + value.getGreeting().getFirstName() + "! ";
            }

            @Override
            public void onError(Throwable t) {
                //client send an error
            }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();
                //this is when we want to return a response (responseObserver)
            }
        };
        return requestObserver;
    }
}
