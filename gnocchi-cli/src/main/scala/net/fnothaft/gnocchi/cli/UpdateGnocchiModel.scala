/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fnothaft.gnocchi.cli

import java.io.File

import net.fnothaft.gnocchi.association._
import net.fnothaft.gnocchi.models._
import net.fnothaft.gnocchi.gnocchiModel._
import net.fnothaft.gnocchi.sql.GnocchiContext._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.bdgenomics.utils.cli._
import org.kohsuke.args4j.{ Argument, Option => Args4jOption }
import org.bdgenomics.adam.cli.Vcf2ADAM
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
//import net.fnothaft.gnocchi.association.Ensembler TODO: pull in the ensembler code or predict won't work.
import net.fnothaft.gnocchi.gnocchiModel.BuildAdditiveLogisticGnocchiModel

import scala.collection.mutable.ListBuffer

object UpdateGnocchiModel extends BDGCommandCompanion {
  val commandName = "UpdateGnocchiModel"
  val commandDescription = "Fill this out later!!"

  def apply(cmdLine: Array[String]) = {
    new UpdateGnocchiModel(Args4j[UpdateGnocchiModelArgs](cmdLine))
  }
}

class UpdateGnocchiModelArgs extends RegressPhenotypesArgs {

  @Args4jOption(required = true, name = "-modelLocation", usage = "The location of the model to load.")
  var modelLocation: String = _

  @Args4jOption(required = true, name = "-saveModelTo", usage = "The location to save model to.")
  var saveTo: String = _

}

class UpdateGnocchiModel(protected val args: UpdateGnocchiModelArgs) extends BDGSparkCommand[UpdateGnocchiModelArgs] {
  override val companion = UpdateGnocchiModel

  override def run(sc: SparkContext) {

    // instantiate regressPhenotypes obj
    val regPheno = new RegressPhenotypes(args)

    // Load in genotype data filtering out any SNPs not provided in command line
    val genotypeStates = regPheno.loadGenotypes(sc)

    // Load in phenotype data
    val phenotypes = regPheno.loadPhenotypes(sc)

    // load model TODO: Write load GnocchiModel object
    //    val model = LoadGnocchiModel(args.modelLocation, sc)

    // update the model with new data
    //    val (gnocchiModel, assocs) = model.update(genotypeStates, phenotypes, sc)

    // save the associations
    //    regPheno.logResults(assocs, sc)

    // save the model
    //    gnocchiModel.save(args.saveTo)
  }

  //  def loadGenotypes(sc: SparkContext): Dataset[GenotypeState] = {
  //    // set up sqlContext
  //    val sqlContext = SQLContext.getOrCreate(sc)
  //    import sqlContext.implicits._
  //
  //    val absAssociationPath = new File(args.associations).getAbsolutePath
  //    var parquetInputDestination = absAssociationPath.split("/").reverse.drop(1).reverse.mkString("/")
  //    parquetInputDestination = parquetInputDestination + "/parquetInputFiles/"
  //    val parquetFiles = new File(parquetInputDestination)
  //
  //    val vcfPath = args.genotypes
  //    val posAndIds = GetVariantIds(sc, vcfPath)
  //
  //    // check for ADAM formatted version of the file specified in genotypes. If it doesn't exist, convert vcf to parquet using vcf2adam.
  //    if (!parquetFiles.getAbsoluteFile.exists) {
  //      val cmdLine: Array[String] = Array[String](vcfPath, parquetInputDestination)
  //      Vcf2ADAM(cmdLine).run(sc)
  //    } else if (args.overwrite) {
  //      FileUtils.deleteDirectory(parquetFiles)
  //      val cmdLine: Array[String] = Array[String](vcfPath, parquetInputDestination)
  //      Vcf2ADAM(cmdLine).run(sc)
  //    }
  //
  //    val genotypes = sqlContext.read.format("parquet").load(parquetInputDestination)
  //    // transform the parquet-formatted genotypes into a dataFrame of GenotypeStates and convert to Dataset.
  //    val genotypeStates = sqlContext
  //      .toGenotypeStateDataFrame(genotypes, args.ploidy, sparse = false)
  //    val genoStatesWithNames = genotypeStates.select(concat($"contig", lit("_"), $"end", lit("_"), $"alt") as "contig",
  //      genotypeStates("start"),
  //      genotypeStates("end"),
  //      genotypeStates("ref"),
  //      genotypeStates("alt"),
  //      genotypeStates("sampleId"),
  //      genotypeStates("genotypeState"),
  //      genotypeStates("missingGenotypes"))
  //    println(genoStatesWithNames.take(10).toList)
  //
  //    // mind filter
  //    genoStatesWithNames.registerTempTable("genotypeStates")
  //
  //    val mindDF = sqlContext.sql("SELECT sampleId FROM genotypeStates GROUP BY sampleId HAVING SUM(missingGenotypes)/(COUNT(sampleId)*2) <= %s".format(args.mind))
  //    var filteredGenotypeStates = genoStatesWithNames.filter($"sampleId".isin(mindDF.collect().map(r => r(0)): _*))
  //    println("Pre-filtered GenotypeStates: " + filteredGenotypeStates.take(5).toList)
  //    if (args.snps != null) {
  //      // Filter out only specified snps
  //      // TODO: Clean this
  //      val snps = args.snps.split(',')
  //      filteredGenotypeStates = filteredGenotypeStates.filter(filteredGenotypeStates("contig").isin(snps: _*))
  //    }
  //    println("Post-filtered GenotypeStates: " + filteredGenotypeStates.take(5).toList)
  //    filteredGenotypeStates.as[GenotypeState]
  //  }
}
