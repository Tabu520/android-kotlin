package taipt4.kotlin.eatitv2.model

class FCMResponse {

    var multicast_id: Long = 0
    var success: Int = 0
    var failure: Int = 0
    var canonical_ids = 0
    var results: List<FCMResult>? = null
    var message_id: Long = 0
}