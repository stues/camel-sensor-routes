camel-sensor-routes
===================

A camel route that 
- converts SBS1 Data from a TCP Channel into GeoJSON Features 
- aggregates the data with information from previous received objects
- adds the GeoJSON Features into a Websocket 
- converts the SBS1 Data into JAXBElements to be inserted into a Sensor Observation Service as InsertObservation
- allows websocket filtering on client level

Use org.apache.camel.spring.Main to start in debugger.

