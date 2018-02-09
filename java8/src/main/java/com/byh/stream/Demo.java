package com.byh.stream;

import com.byh.constants.SampleData;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo {

    public static void StreamDemo(){

      System.out.println("original sample list:" + SampleData.SMAPLE_INT_LIST.toString());

      //map
      List<Integer> mapResultList =  SampleData.SMAPLE_INT_LIST.stream().map(x->x+100).collect(Collectors.toList());
      System.out.println("map demo:" + mapResultList.toString());

      //filter
      List<Integer> filterResultList  = SampleData.SMAPLE_INT_LIST.stream().filter(x->x>3).collect(Collectors.toList());
      System.out.println("filter demo:" + filterResultList.toString());

      //min 返回Optional
      Optional<Integer> minResult = SampleData.SMAPLE_INT_LIST.stream().min(Comparator.comparing(n->n));
      System.out.println("min demo:" + (minResult.isPresent()?minResult.get():-1));

      //min 返回Optional
        Optional<Integer> maxResult = SampleData.SMAPLE_INT_LIST.stream().max(Comparator.comparing(n->n));
        System.out.println("max demo:" + (maxResult.isPresent()?maxResult.get():-1));

      //all match
      boolean allMatchResult = SampleData.SMAPLE_INT_LIST.stream().allMatch(x->x>0);
      System.out.println("all match demo:" + allMatchResult);

      //any match
      boolean anyMatchResult = SampleData.SMAPLE_INT_LIST.stream().allMatch(x->x<0);
      System.out.println("anymatch demo:" + anyMatchResult);

      //none match
      boolean noneMatchResult = SampleData.SMAPLE_INT_LIST.stream().noneMatch(x->x<0);
      System.out.println("nonematch demo:" + noneMatchResult);

      //sorted asc
      List<Integer> sortedResultList = SampleData.SMAPLE_INT_LIST.stream().sorted(Comparator.comparing(x->x)).collect(Collectors.toList());
      System.out.println("asc sorted demo:" + sortedResultList);

      //sorted desc
      List<Integer> sortedDescResultList = SampleData.SMAPLE_INT_LIST.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
      System.out.println("desc sorted demo:" + sortedDescResultList);

      //limit
      List<Integer> limitResultList = SampleData.SMAPLE_INT_LIST.stream().limit(3).collect(Collectors.toList());
      System.out.println("limit demo:" + limitResultList);

      //skip
      List<Integer> skipResultList = SampleData.SMAPLE_INT_LIST.stream().skip(2).collect(Collectors.toList());
      System.out.println("skip demo:" + skipResultList);

      //distinct
      List<Integer> distinctResultList = SampleData.SMAPLE_INT_LIST.stream().distinct().collect(Collectors.toList());
      System.out.println("skip demo:" + distinctResultList);

      //parallel stream
      List<Integer> parallelResultList = SampleData.SMAPLE_INT_LIST.stream().parallel().collect(Collectors.toList());
      System.out.println("parallel demo:" + parallelResultList);

      //groupby
      Map<Integer,List<Integer>> groupByResultMap = SampleData.SMAPLE_INT_LIST.stream().collect(Collectors.groupingBy(x->x));
      System.out.println("groupby demo:" + groupByResultMap);

      //partition
      Map<Boolean,List<Integer>> partitionByResultMap = SampleData.SMAPLE_INT_LIST.stream().collect(Collectors.partitioningBy(x-> x>10));
      System.out.println("partition by >10:" + partitionByResultMap);

      //partition with downstream
      Map<Boolean,Long> partitionByWithDownStreamResultMatp = SampleData.SMAPLE_INT_LIST.stream().collect(Collectors.partitioningBy(x-> x>10
              ,Collectors.counting()));
      System.out.println("partition by >10 with downstream:" + partitionByWithDownStreamResultMatp);

      //reduce withoutIdentity
      Optional<Integer> reduceWithoutIdentityResult = SampleData.SMAPLE_INT_LIST.stream().reduce((souce,item)->{souce+=item;return souce;});
      System.out.println("reduce with Identity:" + (reduceWithoutIdentityResult.isPresent()?reduceWithoutIdentityResult.get():-1));

      //reduce with identity,when stream is null return identity
      Integer reduceWithIdentityResult = SampleData.SMAPLE_INT_LIST.stream().reduce(0,Integer::sum);
      System.out.println("reduce without Identity:" + reduceWithIdentityResult);

      //generate
      Random seed = new Random();
      Supplier<Integer> random =seed::nextInt;
      List<Integer> generateResultList = Stream.generate(random).limit(10).collect(Collectors.toList());
      System.out.println("genereate demo "+generateResultList);

      //generate udf supplier
      List<Integer> generateWithUdfSupplier = Stream.generate(new IntSupplier()).limit(10).collect(Collectors.toList());
      System.out.println("genereate with udf supplier demo "+ generateWithUdfSupplier);

      //iterator
      List<Integer> iteratorListResult = Stream.iterate(0,x->x+3).limit(10).collect(Collectors.toList());
      System.out.println("iterator demo "+ iteratorListResult);

      //average
      double averageResult = SampleData.SMAPLE_INT_LIST.stream().collect(Collectors.averagingInt(x-> x));
      System.out.println("averagingInt demo "+ averageResult);

      //summarizingInt
      IntSummaryStatistics summarizingIntResult = SampleData.SMAPLE_INT_LIST.stream().collect(Collectors.summarizingInt(x-> x));
      System.out.println("summarizingInt demo:");
      System.out.println("---------------max:"+summarizingIntResult.getMax());
      System.out.println("---------------count:"+summarizingIntResult.getCount());
      System.out.println("---------------getMin:"+summarizingIntResult.getMin());
      System.out.println("---------------getSum:"+summarizingIntResult.getSum());
      System.out.println("---------------getAverage:"+summarizingIntResult.getAverage());

    }

}
