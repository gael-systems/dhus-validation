import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

class FilterODataTests extends Simulation {

	// configuration
	// with dummy browser informations
	val httpConf = http
	    .baseURL("http://192.168.0.17:8081/odata/v1") // TODO get base url dynamically
	    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
	    .doNotTrackHeader("1")
	    .acceptLanguageHeader("en-US,en;q=0.5")
	    .acceptEncodingHeader("gzip, deflate")
	    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
	    .basicAuth("root", "rootpassword") // create user root on centos1 dhus

	val sets = csv("entitysets.csv").queue // fields: entitySetName
	val operators = csv("operators.csv").queue // fields: operator

	/* 
	 TODO 
	 	- add feeders that provide filter arguments
	 	- use operator filter in scenario
	*/

	sealed class EntitySet protected (val entitySetName : String, val properties : FeederBuilder[_], val propertyCount : Int) {
		val filter = repeat(propertyCount) {
			exec(http("Service")
				.get("/").check(status.is(200)))
			.pause(1)
			.feed(properties)
			.exec(http(entitySetName+" ${property} eq")
				.get("/"+entitySetName+"?$filter=${property} eq ${eqValue}").check(status.is(200)))
			.pause(1)
			.exec(http(entitySetName+" ${property} ne")
				.get("/"+entitySetName+"?$filter=${property} ne ${neValue}").check(status.is(200)))
		} 
	}

	// fields in properties feeders: property, eqValue, neValue
	object Connections extends EntitySet("Connections", csv("custom-Connections-properties.csv").queue, 9)
	object Networks extends EntitySet("Networks", csv("custom-Networks-properties.csv").queue, 1)
	object Products extends EntitySet("Products", csv("custom-Products-properties.csv").queue, 8)
	object Synchronizers extends EntitySet("Synchronizers", csv("custom-Synchronizers-properties.csv").queue, 16)
	object Users extends EntitySet("Users", csv("custom-Users-properties.csv").queue, 12)
	object Ingests extends EntitySet("Ingests", csv("custom-Ingests-properties.csv").queue, 6)
	object UserSynchronizers extends EntitySet("UserSynchronizers", csv("custom-UserSynchronizers-properties.csv").queue, 15)
	object Collections extends EntitySet("Collections", csv("custom-Collections-properties.csv").queue, 2)
	object Classes extends EntitySet("Classes", csv("custom-Classes-properties.csv").queue, 2)
	object DeletedProducts extends EntitySet("DeletedProducts", csv("custom-DeletedProducts-properties.csv").queue, 10)

	val filterConnections = scenario("Filter Connections").exec(Connections.filter)
	val filterNetworks = scenario("Filter Networks").exec(Networks.filter)
	val filterProducts = scenario("Filter Products").exec(Products.filter)
	val filterSynchronizers = scenario("Filter Synchronizers").exec(Synchronizers.filter)
	val filterUsers = scenario("Filter Users").exec(Users.filter)
	val filterIngests = scenario("Filter Ingests").exec(Ingests.filter)
	val filterUserSynchronizers = scenario("Filter UserSynchronizers").exec(UserSynchronizers.filter)
	val filterCollections = scenario("Filter Collections").exec(Collections.filter)
	val filterClasses = scenario("Filter Classes").exec(Classes.filter)
	val filterDeletedProducts = scenario("Filter DeletedProducts").exec(DeletedProducts.filter)
	
	setUp(
    	filterConnections.inject(rampUsers(1) over (20 seconds)), // scenario will be executed i times over n seconds
    	filterNetworks.inject(rampUsers(1) over (20 seconds)),
    	filterProducts.inject(rampUsers(1) over (20 seconds)),
    	filterSynchronizers.inject(rampUsers(1) over (20 seconds)),
    	filterUsers.inject(rampUsers(1) over (20 seconds)),
    	filterIngests.inject(rampUsers(1) over (20 seconds)),
    	filterUserSynchronizers.inject(rampUsers(1) over (20 seconds)),
    	filterCollections.inject(rampUsers(1) over (20 seconds)),
    	filterClasses.inject(rampUsers(1) over (20 seconds)),
    	filterDeletedProducts.inject(rampUsers(1) over (20 seconds))
  	).assertions(global.failedRequests.percent.is(0))
  	.protocols(httpConf)
}