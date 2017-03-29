# event-by-tag-failure
Just showing when CassandraReadJournal.eventsByTag does not deliver all messages.

This code requires `CASSANDRA` env variable with Cassandra contact point.  
`Agg` is simple domain.  
`TaggingAdapter` tags all events with some tag, which is randomly generated at application start. Thanks to this one does not have to clean keyspace between each run.  
`Main` is main scenario that shows that not all events are being delivered by `CassandraReadJournal.eventsByTag` if both `eventual-consistency-delay = 0s` and `delayed-event-timeout = 0s`.  
It simply creates events for 10000 aggregates.  

Please find `src/main/resources/application.conf` and increase `eventual-consistency-delay` or `delayed-event-timeout` to see that with these values set stream in `Main` eventually completes with all events read.
