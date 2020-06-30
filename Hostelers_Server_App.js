const express = require('express')
const mongoClient = require('mongodb').MongoClient
const bodyparser = require("body-parser")
const fs = require('fs')
const url = "mongodb://localhost:27017"
const app = express()

app.use(bodyparser.urlencoded({extended:true}))
app.use(bodyparser.json())

mongoClient.connect(url, {useUnifiedTopology:true}, (err, db)=>{
    if(err){
        console.log("Error connecting to MongoDB...")
    }
    else{
        console.log("MongoDB connection Successful...");
        const hostelers_db = db.db("hostelers");
        const warden_users = hostelers_db.collection("warden_users");
        const boarder_users = hostelers_db.collection("boarder_users")
        const hostel_list = hostelers_db.collection("hostel_list")
        const boarder_message_details = hostelers_db.collection("boarder_message_details")  // contains all the previous issues, notifications, previous payments
        const warden_message_details = hostelers_db.collection("warden_message_details") // contains all the notifications and new admissions
        
        var padding_id = function(num){ return (num < 10) ? "0"+num : ""+num}
        var padding_pwd = function(num){
                if(num < 10)
                    return "000" + num
                else if(num < 100)
                    return "00"+ num
                else if(num < 1000)
                    return "0"+ num
                else
                    return num
        }
        app.post("/hostelSignUp",(req, response)=>{
            var today = new Date()
            var date = padding_id(today.getDate())+padding_id(today.getMonth()+1)
            var time = padding_id(today.getHours())+padding_id(today.getMinutes())+padding_id(today.getSeconds())
            var w_id = date+time
            var w_pwd = "Hostelers"+padding_pwd(today.getMilliseconds())+padding_id(today.getSeconds())
            var h_name = req.body.hostelName
            var path = "hostelDocs/hostelProofs/"+h_name+".jpeg"
            let buffer = new Buffer(req.body.hostelDocument, "base64")
            fs.writeFileSync(path, buffer)
            const warden_user_details = {
                hostelName :  h_name,
                hostelLocation : req.body.hostelLocation,
                wardenId : w_id,
                wardenName : req.body.wardenName,
                wardenEmail : req.body.wardenEmail,
                wardenNumber : req.body.wardenNumber,
                wardenKey : w_pwd,
                hostelProofDocument : path
            }
            const query = {
                            hostelName: warden_user_details.hostelName,
                            hostelLocation : warden_user_details.hostelLocation,
                            wardenName : warden_user_details.wardenName,
                            wardenNumber : warden_user_details.wardenNumber
                          }
            warden_users.findOne(query,(err, query_result)=>{
                if(query_result == null){
                    hostel_list.insertOne({hostelName : warden_user_details.hostelName,
                                          hostelLocation : warden_user_details.hostelLocation}, (err, _res)=>{
                                              if(err) console.log("hostel insertion error..."); 
                                              else console.log("hostel insertion successful...");
                                         })
                    warden_message_details.insertOne({
                        hostelName : warden_user_details.hostelName,
                        hostelLocation : warden_user_details.hostelLocation,
                        wardenId : warden_user_details.wardenId,
                        warden_notifications : new Array(),
                        new_admissions : new Array()
                    }, (_err, _res)=>{})
                    warden_users.insertOne(warden_user_details, (err, res)=>{
                        const toSendObject = {
                            wardenId : warden_user_details.wardenId,
                            wardenKey : warden_user_details.wardenKey,
                            wardenEmail: warden_user_details.wardenEmail
                        }
                        response.status(200).send(JSON.stringify(toSendObject))
                    })
                }
                else{
                    response.status(409).send()
                }
            })
        })

        app.get("/wardenSignIn/:hostelLocation/:hostelName/:wardenId", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation,
                wardenId : req.params.wardenId,
            }
            warden_users.findOne(query, (err, result)=>{
                if(err){
                    console.log("Error in forgot password...")
                }
                else if(result == null){
                    response.status(404).send()
                }
                else{
                    const objToSend = {
                        email : result.wardenEmail,
                        password : result.wardenKey
                    }
                    response.status(200).send(JSON.stringify(objToSend))
                }
            })    
        })

        app.post("/wardenSignIn", (request, response)=>{
            const query = {
                hostelName : request.body.hostelName,
                hostelLocation : request.body.hostelLocation,
                wardenId : request.body.wardenId,
                wardenKey : request.body.wardenPassword
            }
            warden_users.findOne(query, (err, result)=>{
                if(result == null){
                    response.status(404).send()
                }
                else{
                    const objToSend = {
                        wardenName : result.wardenName,
                        wardenId : result.wardenId
                    }
                    response.status(200).send(JSON.stringify(objToSend))
                }
            })
        })

        

        app.put("/warden/change_password", (req, response)=>{
            const query = {
                wardenId : req.body.id,
                hostelLocation : req.body.hostelLocation,
                hostelName : req.body.hostelName
            }
            const query_update = {
             wardenKey : req.body.newPassword
            }
            warden_users.updateOne(query, {$set:query_update}, (err, result)=>{
                if(err){
                    console.log("change password query error...")
                }
                else {
                    if(result == null){
                        response.status(404).send()
                    }
                    else{
                        response.status(200).send()
                    }
                }
            })
        })

        app.put("/boarder/change_password", (req, response)=>{
            const query = {
                boarderId : req.body.id,
                hostelLocation : req.body.hostelLocation,
                hostelName : req.body.hostelName
            }
            const query_update = {
             boarderPassword : req.body.newPassword
            }
            boarder_users.updateOne(query, {$set:query_update}, (err, result)=>{
                if(err){
                    console.log("change password query error...")
                }
                else {
                    if(result == null){
                        response.status(404).send()
                    }
                    else{
                        response.status(200).send()
                    }
                }
            })
        })

        app.post("/hostel_list", (_req,response)=>{
            hostel_list.find().toArray((_err, result)=>{
                if(result.length == 0){
                    response.status(404).send()
                }
                else{
                    response.status(200).send(JSON.stringify(result))
                }
            })
        })


        // also add the new boarder details into the new admissions collection
        app.post("/boarderSignUp", (req, response)=>{
            var date = new Date()
            const joiningDate = date.getDate()+"-"+(date.getMonth()+1)+"-"+date.getFullYear()
            var boarderId = "Boarder"+padding_id(date.getSeconds())+padding_pwd(date.getMilliseconds())+padding_id(date.getMinutes())
            var boarderPwd = "MyHostel"+Math.round(Math.random()*10)+padding_id(date.getMinutes())+padding_pwd(date.getMilliseconds())
            const bName = req.body.boarderName
            const idPath = "hostelDocs/boarderIds/"+bName+".jpeg"
            const photoPath = "hostelDocs/boarderPhotos/"+bName+".jpeg"
            let idBuffer = new Buffer(req.body.idProof, "base64")
            let photoBuffer = new Buffer(req.body.photo, "base64")
            fs.writeFileSync(idPath, idBuffer)
            fs.writeFileSync(photoPath, photoBuffer)
            const details = {
                boarderId : boarderId,
                boarderName : bName,
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation,
                fatherName : req.body.fatherName,
                boarderJob : req.body.boarderJob,
                boarderEmail : req.body.email,
                boarderMobile : req.body.mobileNumber,
                idProof : idPath,
                photo : photoPath,
                paymentMethod : req.body.paymentMethod,
                joiningDate : joiningDate,
                boarderPassword : boarderPwd,
                roomNumber : null
            }
            const query = {
                hostelName : details.hostelName,
                hostelLocation : details.hostelLocation,
                boarderMobile : details.boarderMobile,
                boarderEmail : details.boarderEmail
            }
            boarder_users.findOne(query, (_err, findResult)=>{
                if(findResult == null){
                    var wardenName, wardenNumber, wardenEmail;
                    const warden_find_query = {hostelName:details.hostelName,hostelLocation: details.hostelLocation}
                    warden_message_details.findOne(warden_find_query, (_err, findRes)=>{
                        var new_joinings = findRes.new_admissions
                        const new_admission_object = {
                            boarderId : details.boarderId,
                            boarderName : details.boarderName,
                            fatherName : details.fatherName,
                            boarderJob : details.boarderJob,
                            boarderMobile : details.boarderMobile,
                            idProof : details.idProof,
                            photo : details.photo,
                            joiningDate : details.joiningDate,
                            roomNumber : details.roomNumber,
                            boarderEmail : details.boarderEmail
                        }
                        new_joinings.unshift(new_admission_object)
                        warden_message_details.updateOne({}, {$set:{new_admissions : new_joinings}}, (_err, admissionUpdate)=>{
                            if(err){
                                console.log("Error in admission update")
                            }
                            else{
                                console.log("admission is inserted")
                            }
                        })
                        
                    })
                    warden_users.findOne(warden_find_query, (_err, wardenResult)=>{
                        if(wardenResult != null){
                            wardenName = wardenResult.wardenName,
                            wardenNumber = wardenResult.wardenNumber,
                            wardenEmail = wardenResult.wardenEmail
                        }
                    })
                    boarder_message_details.insertOne({
                        boarderId : details.boarderId,
                        hostelName : details.hostelName,
                        hostelLocation : details.hostelLocation,
                        notifications : ["Hello welcome to our hostel!"],
                        previous_issues : new Array(),
                        previous_payments : new Array()
                    }, (err, boarder_message_details_result)=>{
                        if(err){
                            console.log("boarder message details collection insert error...");
                        }
                        else{
                            boarder_message_details_result
                            console.log("boarder message details inserted");
                        }
                    })
                    boarder_users.insertOne(details, (err, insertResult)=>{
                        const detailsToSend = {
                            boarderName : details.boarderName,
                            boarderId : details.boarderId,
                            hostelName : details.hostelName,
                            wardenName : wardenName,
                            wardenMobile : wardenNumber,
                            wardenEmail : wardenEmail,
                            boarderEmail : details.boarderEmail,
                            boarderPassword : details.boarderPassword
                        }
                        response.status(200).send(JSON.stringify(detailsToSend))
                    })
                }
                else{
                    response.status(409).send()
                }
            })
        })

        app.post("/boarderSignIn", (req, response)=>{
            const query = {
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation,
                boarderId : req.body.boarderId,
                boarderPassword : req.body.boarderPassword
            }
            boarder_users.findOne(query, (_err, findResult)=>{
                if(findResult == null){
                    response.status(404).send()
                }else{
                    let buffer = fs.readFileSync(findResult.photo)
                    var photoEncode = buffer.toString("base64")
                    const dataToSend = {
                        boarderName : findResult.boarderName,
                        boarderId : findResult.boarderId,
                        boarderPhoto : photoEncode
                    }
                    response.status(200).send(JSON.stringify(dataToSend))
                }
            })
        })

        app.get("/boarderSignIn/:hostelLocation/:hostelName/:boarderId", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation,
                boarderId : req.params.boarderId
            }
            boarder_users.findOne(query, (_err, findResult)=>{
                if(findResult == null){
                    response.status(404).send()
                }else{
                    const dataToSend = {
                        email : findResult.boarderEmail,
                        password : findResult.boarderPassword
                    }
                    response.status(200).send(JSON.stringify(dataToSend))
                }
            })
        })

        app.get("/warden/boarder_list/:hostelName/:hostelLocation", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation
            }
            
            boarder_users.find(query).sort({boarderName:-1}).toArray((err, result)=>{
                if(err){
                    console.log("boarder list error...")
                }
                else{
                    if(result.length == 0){
                        response.status(404).send()
                    }
                    else{
                        let arrayResult = result.map((boarderDetails, _index, _array)=>{
                            const details = {
                                id : boarderDetails.boarderId,
                                name : boarderDetails.boarderName
                            }
                            return details
                        })
                        response.status(200).send(JSON.stringify(arrayResult))
                    }
                }
            })

        })


        app.put("/notify_boarder", (req, response)=>{
            const query = {
                boarderId : req.body.boarderId,
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation
            }
            boarder_message_details.findOne(query,(err, findResult)=>{
                if(err){
                    console.log("notify boarder error...")
                }
                else{ 
                    findResult.notifications.unshift(req.body.message)
                    boarder_message_details.updateOne(query, {$set: {notifications : findResult.notifications}},(err, result)=>{
                        if(err){
                            console.log("[notify boarder] update error...")
                        }
                        else{
                            //console.log(findResult)
                            response.status(200).send()
                        }
                    })
                }
            })
        })

        app.get("/boarder_notifications/:hostelName/:hostelLocation/:boarderId", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation: req.params.hostelLocation,
                boarderId : req.params.boarderId
            }

            boarder_message_details.findOne(query, (err, findResult)=>{
                if(err){
                    console.log("boarder notifications send error")
                }
                if(findResult != null){
                    var messages = findResult.notifications
                    if(messages.length == 0)
                        response.status(404).send()
                    else{
                         messages = messages.map((message, index, array)=>{
                            return {
                                message : message
                            }
                        })
                        response.status(200).send(JSON.stringify(messages))
                    }
                }
            })
        })

        app.delete("/boarder_notifications/delete_notification/:hostelName/:hostelLocation/:boarderId/:notification_position", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation: req.params.hostelLocation,
                boarderId : req.params.boarderId
            }
            boarder_message_details.findOne(query, (err, findResult)=>{
                if(err){
                    console.log("boarder notifications delete error")
                }
                if(findResult != null){
                    if(findResult.notifications.length > 0)
                    {
                        const messages = findResult.notifications
                        messages.splice(parseInt(req.params.notification_position),1)
                        boarder_message_details.updateOne({}, {$set: {notifications : messages}},(err, result)=>{
                            if(err){
                                console.log("[notify boarder] update error...")
                            }
                            else{
                                response.status(200).send()
                            }
                        })
                        
                    }
                }
            })
        })

        app.get("/warden/boarder_admissions/:hostelName/:hostelLocation/:wardenId", (req, response)=>{
            const query = {
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation,
                wardenId : req.params.wardenId
            }
            warden_message_details.findOne(query, (_err, findResult)=>{
                if(findResult != null){
                    var admissions_result = findResult.new_admissions.map((obj, index, array)=>{
                        var id_proof_string = fs.readFileSync(obj.idProof).toString("base64")
                        var photo_string = fs.readFileSync(obj.photo).toString("base64")
                        return {
                            boarderId : obj.boarderId,
                            boarderName : obj.boarderName,
                            boarderJob : obj.boarderJob,
                            boarderMobile : obj.boarderMobile,
                            idProof : id_proof_string,
                            photo : photo_string,
                            joiningDate : obj.joiningDate,
                            roomNumber : obj.roomNumber,
                            fatherName : obj.fatherName,
                            boarderEmail : obj.boarderEmail
                        }
                    })
                    response.status(200).send(JSON.stringify(admissions_result))
                }
            })
        })

        app.put("/warden/boarder_allocate_room",(req,response)=>{
            const warden_query = {
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation,
                wardenId : req.body.wardenId,
            }
            const boarder_query = {
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation,
                boarderId : req.body.boarderId
            }
            warden_message_details.findOne(warden_query, (_err, findWardenResult)=>{
                if(findWardenResult != null){
                  findWardenResult.new_admissions.find((value, index, array)=>{
                        return value.boarderId === boarder_query.boarderId
                    }).roomNumber = req.body.roomNumber
                  warden_message_details.updateOne(warden_query, {$set : {new_admissions : findWardenResult.new_admissions}}, (err, wUpdateResult)=>{
                            if(err){
                                console.log("[warden message details] boarder room update error...")    
                            }
                            else{
                                boarder_users.updateOne(boarder_query, {$set : {roomNumber : req.body.roomNumber}}, (err, bUpdateResult)=>{
                                    if(err){
                                        console.log("boarder room update error...")
                                    }
                                    else{
                                        response.status(200).send()
                                    }
                               })
                            }
                  })
                  
                }
            })
            
        })

        app.get("/boarder/previous_issues/:hostelName/:hostelLocation/:boarderId", (req, response)=>{
            const query = {
                boarderId : req.params.boarderId,
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation
            }
            boarder_message_details.findOne(query, (_err, result)=>{
                if(result != null){
                    response.status(200).send(JSON.stringify(result.previous_issues))
                }
            })
        })

        app.put("/boarder/create_new_issue", (req, response)=>{
            const boarder_query = {
                boarderId : req.body.boarderId,
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation
            }
            const warden_query = {
                hostelName : req.body.hostelName,
                hostelLocation : req.body.hostelLocation
            }
            var room;
            boarder_users.findOne(boarder_query, (_err, userResult)=>{
                if(userResult != null){
                    room = userResult.roomNumber
                }
            })
            boarder_message_details.findOne(boarder_query, (_err, bFindResult)=>{
                if(bFindResult != null)
                {
                    const issue = {
                        issueType : req.body.issueType,
                        typeCategory : req.body.typeCategory,
                        issueDescription : req.body.issueDescription
                    }
                    bFindResult.previous_issues.unshift(issue)
                    boarder_message_details.updateOne(boarder_query, {$set : {previous_issues : bFindResult.previous_issues}}, (err, bupdateResult)=>{
                        if(err){
                            console.log("issues error")
                        }
                        else{
                            response.status(200).send()
                        }
                    })
                
                }
            })
            warden_message_details.findOne(warden_query, (_err, wFindResult)=>{
                if(wFindResult != null){
                    const notification = {
                        boarderId : req.body.boarderId,
                        issueType : req.body.issueType,
                        typeCategory : req.body.typeCategory,
                        issueDescription : req.body.issueDescription,
                        roomNumber : room
                    }
                    wFindResult.warden_notifications.unshift(notification)
                    warden_message_details.updateOne(warden_query, {$set : {warden_notifications : wFindResult.warden_notifications}}, (err, wUpdateResult)=>{
                        if(err){
                            console.log("warden notification error")
                        }
                        else{
                            console.log("warden notification added")
                        }
                    })
                }
            })
        })

        app.get("/warden/notifications/:hostelName/:hostelLocation/:wardenId", (req, response)=>{
            const query = {
                wardenId : req.params.wardenId,
                hostelName : req.params.hostelName,
                hostelLocation : req.params.hostelLocation
            }
            warden_message_details.findOne(query, (_err, result)=>{
                if(result != null){
                    response.status(200).send(JSON.stringify(result.warden_notifications))
                }
            })
        })


    }// ending else block
})

app.listen(3000,()=>{
    console.log("Listening to port 3000...")
})