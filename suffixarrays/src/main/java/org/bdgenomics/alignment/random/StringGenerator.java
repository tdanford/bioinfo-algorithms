package org.bdgenomics.alignment.random;

import java.util.Random;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class StringGenerator implements Runnable {

  @Option(name="-l", usage="The length of the string to generate", required=true)
  private Integer length;

  @Option(name="-n", usage="The number of strings to generate", required=true)
  private Integer count;

  @Option(name="-a", usage="The alphabet to use", required=false)
  private String alphabet = "ACGT";

  public static void main(String[] args) {
    new StringGenerator(args).run();
  }

  public StringGenerator(String[] args) {
    CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);

    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      System.err.print("StringGenerator ");
      parser.printSingleLineUsage(System.err);
      System.err.println();
      System.exit(1);
    }
  }

  public void run() {
    RandomStringGenerator gen = new RandomStringGenerator(new Random(), alphabet);
    for(int i = 0; i < count; i++) {
      System.out.println(String.format(">random_%05d", i));
      String randomString = gen.nextString(length);
      for(int k = 0; k < randomString.length(); k += 80) {
        System.out.println(randomString.substring(k, Math.min(k + 80, randomString.length())));
      }
    }
  }

}
