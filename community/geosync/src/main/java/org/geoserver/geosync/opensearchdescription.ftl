<?xml version="1.0" encoding="UTF-8"?>
<OpenSearchDescription 
    xmlns="http://a9.com/-/spec/opensearch/1.1/" 
    xmlns:geo="http://a9.com/-/opensearch/extensions/geo/1.0/" 
    xmlns:time="http://a9.com/-/opensearch/extensions/time/1.0/">

    <ShortName>Geo-Sync Search</ShortName>
    <Description>Geo-Synchronization Service Search Interface</Description>
    <Contact>${CONTACT_ADDRESS}</Contact>
    <Url type="application/atom+xml"
        template="${GEOSERVER_URL}history?bbox={geo:box?}&amp;polygon={geo:polygon?}&amp;dtstart={time:start?}&amp;dtend={time:end?}&amp;count={count}&amp;startIndex={startIndex?}&amp;format=atom"
     />
    <LongName>Geo-Synchronization Service Search Interface</LongName>
    <Query role="example" searchTerms="cat" />
    <Language>en-us</Language>
</OpenSearchDescription>
