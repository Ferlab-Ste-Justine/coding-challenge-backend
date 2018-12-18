# Coding Challenge
Fork this repository, and submit instructions on how to preview the product.

### Project Guidelines
* Read data from the Product Dataset listed below
* Harmonize [original object data](https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/tweet-object.html) into the final format defined in `format.json`
* As part of harmonization, the words contained in the `text` key should be inversed 
  * E.g. "The lazy fox jumped over the computer." becomes "Computer the over jumped fox lazy the."
* The harmonized data should be stored into `database.csv`
* Data stored within `database.csv` should be sorted alphabetically by `screen_name`

### Technical Guidelines
* Java, Scala or Go
* Command-line user interface

### Product Dataset
* Track the keyword `cats` from [Twitter's Streaming API](https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html)
