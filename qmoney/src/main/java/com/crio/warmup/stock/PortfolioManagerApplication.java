
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    File file = resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();
    PortfolioTrade[] portfolioTrade = objectMapper.readValue(file, PortfolioTrade[].class);
    List<String> symbols = new ArrayList<String>();
    for(PortfolioTrade pT : portfolioTrade){
      // System.out.println(pT.toString());
      symbols.add(pT.getSymbol());
    }

    return symbols;
  }

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "assessments/trades.json";
    String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/harshit-criodo-ME_QMONEY_V2/qmoney/bin/test/assessments/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@815b41f";
    String functionNameFromTestFileInStackTrace = "mainReadFile()";
    String lineNumberFromTestFileInStackTrace = "18";


   return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
       toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
       lineNumberFromTestFileInStackTrace});
 }

 public static Double getClosingPriceOnEndDate(List<Candle> candles) {
  return candles.get(candles.size() - 1).getClose();
}


public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
  String url = prepareUrl(trade, endDate, token);
  RestTemplate restTemplate = new RestTemplate();
  Candle[] candles = restTemplate.getForObject(url, TiingoCandle[].class);
  return Arrays.asList(candles);
  
}



  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>

  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {

    // List<PortfolioTrade> res = readTradesFromJson(args[0]);
    // RestTemplate restTemplate = new RestTemplate();
    // List<TotalReturnsDto> make = new ArrayList<>();
    // List<String> fin = new ArrayList<>();
    // for (PortfolioTrade p : res) {
    //   String url = prepareUrl(p, LocalDate.parse(args[1]), getToken());
    //   TiingoCandle[] obj = restTemplate.getForObject(url, TiingoCandle[].class);
    //   make.add(new TotalReturnsDto(p.getSymbol(), obj[obj.length - 1].getClose()));
    // }
    // Collections.sort(make, new Comparator<TotalReturnsDto>() {
    // @Override
    // public int compare(TotalReturnsDto obj1, TotalReturnsDto obj2) {
    //   return (int)(obj1.getClosingPrice()-obj2.getClosingPrice());
    // }
    // });
    // for (TotalReturnsDto td : make) {
    //   fin.add(td.getSymbol());
    // }
    // return fin;
    //File input = resolveFileFromResources(args[0]);
    // LocalDate endDate;
    // try {
    // endDate=LocalDate.parse(args[1]);
    // } catch (DateTimeParseException e) {
    // throw new RuntimeException();
    // }
    //ObjectMapper objMapper = getObjectMapper();
    //PortfolioTrade[] pma = objMapper.readValue(input, PortfolioTrade[].class);
    // if(pma.length==0) return Collections.emptyList();
    List<PortfolioTrade> pma = readTradesFromJson(args[0]);
    List<TotalReturnsDto> list = new ArrayList<>();
    for (int i = 0; i < pma.size(); i++) {
      // if (pma[i] == null || pma[i].getPurchaseDate().compareTo(endDate) > 0)
      // throw new RuntimeException();
      //String url = prepareUrl(pma.get(i), LocalDate.parse(args[1]), getToken());
      // RestTemplate restTemplate = new RestTemplate();
      // TiingoCandle[] tiingoCandle = restTemplate.getForObject(url, TiingoCandle[].class);
      // // if (tiingoCandle.length == 0)
      //   return Collections.emptyList();
      // Double closingPrice = tiingoCandle[tiingoCandle.length - 1].getClose();
      List<Candle> candles = fetchCandles(pma.get(i), LocalDate.parse(args[1]), getToken());
      Double closingPrice=getClosingPriceOnEndDate(candles);
      list.add(new TotalReturnsDto(pma.get(i).getSymbol(), closingPrice));
    }
    Collections.sort(list, new Comparator<TotalReturnsDto>() {
      @Override
      public int compare(TotalReturnsDto p1, TotalReturnsDto p2) {
        return p1.getClosingPrice() > p2.getClosingPrice() ? 1 : -1;
      }
    });
    List<String> ans = new ArrayList<>();
    for (TotalReturnsDto t : list) {
      ans.add(t.getSymbol());
    }
    return ans;
  }
  
  static String getToken() {
    return "d324b9d9b375e8c929aa0c6982a97c49473d4ed7"; 
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
      // ObjectMapper om = getObjectMapper();
      // PortfolioTrade[] pf = om.readValue(resolveFileFromResources(filename), PortfolioTrade[].class);
      // List<PortfolioTrade> ls = Arrays.asList(pf);
      // return ls;
      ObjectMapper objectMapper = getObjectMapper();
      PortfolioTrade[] trades =
          objectMapper.readValue(readFileAsString(filename), PortfolioTrade[].class);
      return Arrays.asList(trades);
  }

  private static String readFileAsString(String filename) throws URISyntaxException, IOException {
    return new String(Files.readAllBytes(resolveFileFromResources(filename).toPath()), "UTF-8");
  }

  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    String url = "https://api.tiingo.com/tiingo/daily/" + trade.getSymbol()
    + "/prices?startDate="
    + trade.getPurchaseDate() + "&endDate=" + endDate.toString()
    + "&token="+token;
return url;
  }


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());


    printJsonObject(mainReadQuotes(args));


  }
}

