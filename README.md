# chaincode
chaincode for storing the data in to blockchain
Assignment:

1.	Steps for installing and instantiating the Chain code on HLF2.2
    //Setting up the Hyperledger Fabric Test network
  	step1-sudo chmod 666 /var/run/docker.sock  //installing docker
    step2-sudo curl -sSL https://bit.ly/2ysbOFE | bash -s -- 2.4.2 1.5.2  //hyperledger fabric installation
  	step3-sudo ./network.sh up -ca -s couchdb  //Start the test network
  	step4-sudo ./network.sh createChannel -c mychannel //create a communication channel
  	step5-peer lifecycle chaincode package storedata.tar.gz --path chaincode/storedata --lang java --label storedata_1
          //Package the Chaincode
  	step6-peer lifecycle chaincode install storedata.tar.gz//Install the Chaincode on Peers
  	step7-peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com \
    --channelID mychannel --name storedata --version 1.0 --package-id <PACKAGE_ID> --sequence 1 \
    --tls --cafile $ORDERER_CA
  	//Approve the Chaincode
  	step8-peer lifecycle chaincode checkcommitreadiness --channelID mychannel --name storedata --version 1.0 --sequence 1 \
    --output json
  	//Check Commit Readiness
  	step9-peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com \
    --channelID mychannel --name storedata --version 1.0 --sequence 1 \
    --tls --cafile $ORDERER_CA \
    --peerAddresses localhost:7051 --tlsRootCertFiles $PEER0_ORG1_CA \
    --peerAddresses localhost:9051 --tlsRootCertFiles $PEER0_ORG2_CA
    //Commit the Chaincode
  	step10-peer lifecycle chaincode querycommitted --channelID mychannel --name storedata
  	//Verify the Committed Chaincode
  	step11-invoke chaincode
  	step12-querry chaincode





2.	Explain Cryptogen and Configtxgen

 * Crypto-config.yaml : This file assists the users to generate public and private keys, digital certificates for peers, and 
ordered service using the command cryptogen.
"cryptogen generate --config=./crypto-config.yaml --output ./crypto-config"

*Configtx.yaml:  This file assists to generate genesis block, which is an appropriate block in Blockchain.
"configtxgen -profile OrdererGenesis -channelID system-channel -outputBlock ./channel-artifacts/genesis.block"


   

			

3.	Develop a chaincode for storing the data in to blockchain		
a.	Store
b.	Retrieve
c.	Update
d.	GetHistory
e.	GetbyNonPrimaryKey (Using CouchDB Rich Queries)
