/*
 * This file contains a reviver for 'JSON.parse' and a replacer for JSON.stringify.
 * These two functions must ALWAYS be used when dealing with json. This allows a consistent 
 * serialization/deserialization process.
 * 
 * This adaptions in this file must match to your implementation of the JsonHelper class in Java!!!
 */

function jsonParseReviver(key, value) {
    if(typeof value === 'string' && value.length === 29) {
        try {
            var valueAsDate = new Date(value);
            if(valueAsDate.toString() !== 'Invalid Date') {
                return valueAsDate;
            }
        } catch(exception) {
            // do nothing... it's simply not a date... ;-)
        }
    }
    
    return value;
}

function jsonStringifyReplacer(key, value) {
    return value;
}