package de.paesserver.iotaApi.webApi;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebApi extends NodeApi {

    public WebApi(String protocol, String host, int port) {
        super(protocol,host,port);
    }

    public WebApi(String protocol, String host) {
        super(protocol,host,8081);
    }

    public String getInfo() {
        //Form: /info
        /*
        version 	String 	Version of GoShimmer.
        networkVersion 	uint32 	Network Version of the autopeering.
        tangleTime 	TangleTime 	TangleTime sync status
        identityID 	string 	Identity ID of the node encoded in base58.
        identityIDShort 	string 	Identity ID of the node encoded in base58 and truncated to its first 8 bytes.
        publicKey 	string 	Public key of the node encoded in base58
        messageRequestQueueSize 	int 	The number of messages a node is trying to request from neighbors.
        solidMessageCount 	int 	The number of solid messages in the node's database.
        totalMessageCount 	int 	The number of messages in the node's database.
        enabledPlugins 	[]string 	List of enabled plugins.
        disabledPlugins 	[]string 	List if disabled plugins.
        mana 	Mana 	Mana values.
        manaDelegationAddress 	string 	Mana Delegation Address.
        mana_decay 	float64 	The decay coefficient of bm2.
        scheduler 	Scheduler 	Scheduler is the scheduler used.
        rateSetter 	RateSetter 	RateSetter is the rate setter used.
        error 	string 	Error message. Omitted if success.
         */
        /*

        Type TangleTime

        field 	Type 	Description
        messageID 	string 	ID of the last confirmed message.
        time 	int64 	Issue timestamp of the last confirmed message.
        synced 	bool 	Flag indicating whether node is in sync.
         */
        /*

        Type Scheduler

        field 	Type 	Description
        running 	bool 	Flag indicating whether Scheduler has started.
        rate 	string 	Rate of the scheduler.
        nodeQueueSizes 	map[string]int 	The size for each node queue.
         */
        /*

        Type RateSetter

        field 	Type 	Description
        rate 	float64 	The rate of the rate setter..
        size 	int 	The size of the issuing queue.
         */
        /*

        Type Mana

        field 	Type 	Description
        access 	float64 	Access mana assigned to the node.
        accessTimestamp 	time.Time 	Time when the access mana was calculated.
        consensus 	float64 	Consensus mana assigned to the node.
        consensusTimestamp	time.Time 	Time when the consensus mana was calculated.
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "info").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMessage(String messageID) {
        //Form:/messages/:messageID
        //Exp: http://localhost:8080/messages/CTNkpA111V33G3CSTxFvpJneqTgsq9rsNqamB7hUsBAv
        /*
         id 	string 	Message ID.
         strongParents 	[]string 	List of strong parents' message IDs.
         weakParents 	[]string 	List of weak parents' message IDs.
         strongApprovers 	[]string 	List of strong approvers' message IDs.
         weakApprovers 	[]string 	List of weak approvers' message IDs.
         issuerPublicKey 	[]string 	Public key of issuing node.
         issuingTime 	int64 	Time this message was issued
         sequenceNumber 	uint64 	Message sequence number.
         payloadType 	string 	Payload type.
         payload 	[]byte 	The contents of the message.
         signature 	string 	Message signature.
         error 	string 	Error message. Omitted if success.
         */

        try {
            return doGetRequest(new URL(getBaseURL() + "messages/" + messageID).openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMessageMeta(String messageID) {
        //Form: /messages/:messageID/metadata
        /*
         id  string 	Message ID.
         receivedTime 	int64 	Time when message was received by the node.
         solid 	bool 	Flag indicating whether the message is solid.
         solidificationTime 	int64 	Time when message was solidified by the node.
         structureDetails 	StructureDetails 	List of weak approvers' message IDs.
         branchID 	string 	Name of branch that the message is part of.
         scheduled 	bool 	Flag indicating whether the message is scheduled.
         booked 	bool 	Flag indicating whether the message is booked.
         eligible 	bool 	Flag indicating whether the message is eligible.
         invalid 	bool 	Flag indicating whether the message is invalid.
         finalized 	bool 	Flag indicating whether the message is finalized.
         finalizedTime 	string 	Time when message was finalized.
         error 	string 	Error message. Omitted if success.
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "messages/" + messageID + "/metadata").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMessageConsensus(String messageID) {
        //Form: /messages/:messageID/consensus
        /*
         id 	string 	Message ID.
         opinionFormedTime 	int64 	Time when the node formed full opinion.
         payloadOpinionFormed 	bool 	Flag indicating whether the node formed opinion about the payload.
         timestampOpinionFormed 	bool 	Flag indicating whether the node formed opinion about the timestamp.
         messageOpinionFormed 	bool 	Flag indicating whether the node formed opinion about the message.
         messageOpinionTriggered 	bool 	Flag indicating whether the node triggered an opinion formed event for the message.
         timestampOpinion 	string 	Opinion about the message's timestamp.
         timestampLoK 	bool 	Level of knowledge about message's timestamp.
         error 	string 	Error message. Omitted if success.
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "messages/" + messageID + "/consensus").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendMessageData(String data) {
        //Form: /data
        /*
        id 	string 	Message ID of the message. Omitted if error.
        error 	string 	Error message. Omitted if success.
         */
        //Create data
        byte[] dataByteArray = Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
        String input = "{\"data\": \"%s\"}";
        input = String.format(input, new String(dataByteArray));


        try {
            if (getProtocol().equals("http") || getProtocol().equals("https")) {
                HttpURLConnection httpURLConnection;
                URLConnection urlConnection = new URL(getBaseURL() + "data").openConnection();
                return doPostRequest(input, urlConnection);
            } else {
                throw new IllegalArgumentException("Invalid protocol: " + getProtocol());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMessagePayload() {
        //From: /messages/payload
        /*
        Required or Optional 	required
        Description 	payload bytes
        Type 	base64 serialized bytes
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "messages/payload").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendMessagePayload(String payload) {
        //Form /messages/payload
        /*
        id 	string 	Message ID of the message. Omitted if error.
        error 	string 	Error message. Omitted if success.
         */
        byte[] dataByteArray = Base64.getEncoder().encode(payload.getBytes(StandardCharsets.UTF_8));
        String input = "{\"payload\": \"%s\"}";
        input = String.format(input, new String(dataByteArray));

        System.out.println("Input: " + input);

        try {
            if (getProtocol().equals("http") || getProtocol().equals("https")) {

                URLConnection urlConnection = new URL(getBaseURL() + "messages/payload").openConnection();
                return doPostRequest(input, urlConnection);
            } else {
                throw new IllegalArgumentException("Invalid protocol: " + getProtocol());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHealthZ() {
        //Form: /healthz
        //Empty response with HTTP 200 success code if everything is running correctly. Error message is returned if failed.
        return null;
    }

    public String getAllNeighbors(boolean allKnown) {
        //Form: /autopeering/neighbors
        //All true form: /autopeering/neighbors?known=1
        /*
        Return field 	Type 	Description
        known 	[]Neighbor 	List of known peers. Only returned when parameter is set.
        chosen 	[]Neighbor 	List of chosen peers.
        accepted 	[]Neighbor 	List of accepted peers.
        error 	string 	Error message. Omitted if success.

        Type Neighbor

        field 	Type 	Description
        id 	string 	Comparable node identifier.
        publicKey 	string 	Public key used to verify signatures.
        services 	[]PeerService 	List of exposed services.

        Type PeerService

        field 	Type 	Description
        id 	string 	Type of service.
        address 	string 	Network address of the service.
         */
        String url = allKnown ? getBaseURL() + "autopeering/neighbors?known=1" : getBaseURL() + "autopeering/neighbors";
        try {
            return doGetRequest(new URL(url).openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNeighbors() {
        return getAllNeighbors(false);
    }

    public String getDrngCollectiveBeacon() {
        //Form: /drng/collectiveBeacon
        /*
        id 	string 	Message ID of beacon message. Omitted if error.
        error 	string 	Error message. Omitted if success.
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "drng/collectiveBeacon").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDrngInfoCommittee() {
        //Form: /drng/info/committee
        /*
        Return field 	Type 	Description
        committees 	[]Committee 	A list of DRNG committees.
        error 	string 	Error message. Omitted if success.

        Type Committee

        field 	Type 	Description
        instanceID 	string 	The identifier of the dRAND instance.
        threshold 	string 	The threshold of the secret sharing protocol.
        identities 	float64 	The nodes' identities of the committee members.
        distributedPK 	string 	Distributed Public Key of the committee
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "drng/info/committee").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDrngInfoRandomness() {
        //Form: /drng/info/randomness
        /*
        Return field 	Type 	Description
        randomness 	[]Randomness 	List of randomness
        error 	string 	Error message. Omitted if success.

        Type Randomness

        field 	Type 	Description
        instanceID 	uint32 	The identifier of the dRAND instance.
        round 	uint64 	The current DRNG round.
        timestamp 	time.Time 	The timestamp of the current randomness message
        randomness 	[]byte 	The current randomness as a slice of bytes
         */
        try {
            return doGetRequest(new URL(getBaseURL() + "drng/info/randomness").openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFaucet() {
        //Form: /faucet
        /*
        Parameter 	address
        Required or Optional 	required
        Description 	address to pledge funds to
        Type 	string

        Parameter 	accessManaPledgeID
        Required or Optional 	optional
        Description 	node ID to pledge access mana to
        Type 	string

        Parameter 	consensusManaPledgeID
        Required or Optional 	optional
        Description 	node ID to pledge consensus mana to
        Type 	string

        Parameter 	powTarget
        Required or Optional 	required
        Description 	proof of the PoW being done, only used in HTTP api
        Type 	uint64

        Parameter 	nonce
        Required or Optional 	required
        Description 	target Proof of Work difficulty,only used in client lib
        Type 	uint64
         */
        //TODO getFaucet
        return null;
    }

    public String sendFaucetRequest(String base58EncodedAddr, int powTarget, String... pledgeIDs) {
        //
        /*
        Return field 	Type 	Description
        id 	string 	Message ID of the faucet request. Omitted if error.
        error 	string 	Error message. Omitted if success.
         */
        //TODO sendFaucetRequest
        return null;
    }


}
