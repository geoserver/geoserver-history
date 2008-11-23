 <#if LAYER == "">
   <#assign shortName="Changes Search"/>
   <#assign description="GeoServer Changes Search Interface"/>
 <#else>
   <#assign shortName="${LAYER} Changes Search"/>
   <#assign description="${LAYER} Changes Search Interface"/>
 </#if>

<OpenSearchDescription 
    xmlns="http://a9.com/-/spec/opensearch/1.1/" 
    xmlns:geo="http://a9.com/-/opensearch/extensions/geo/1.0/" 
    xmlns:time="http://a9.com/-/opensearch/extensions/time/1.0/"> 
    
    <ShortName>${LAYER} Changes Search</ShortName>
    <Description>${LAYER} Changes Search Interface</Description>
    <Contact>${CONTACT_ADDRESS}</Contact>
    <Url type="application/atom+xml"
        template="${BASE_URL}?bbox={geo:box?}&amp;dtstart={time:start?}&amp;dtend={time:end?}&amp;startIndex={startIndex?}&amp;format=atom"
     />
    <Query role="example" searchTerms="cat" />
    <Language>en-us</Language>
    
</OpenSearchDescription>
