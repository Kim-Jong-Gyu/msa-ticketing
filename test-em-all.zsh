# script를 통해 마이크로 서비스 환경을 자동으로 시작하고, 필요한 모든 테스트를 실행


#
#
: ${HOST=localhost}
: ${PORT=8080}


function setUpTestdata() {
  local inputHall

  inputHall='{"hallId" : 1, "hallName" : "name",
    "seatList" :[
            {"seatNumber":1, "section":"a", "type":"VIP"},
            {"seatNumber":2, "section":"a", "type":"STANDARD"},
            {"seatNumber":3, "section":"a", "type":"STANDARD"},
            {"seatNumber":4, "section":"a", "type":"STANDARD"},
            {"seatNumber":1, "section":"b", "type":"VIP"},
            {"seatNumber":2, "section":"b", "type":"STANDARD"},
            {"seatNumber":3, "section":"b", "type":"STANDARD"},
            {"seatNumber":4, "section":"b", "type":"STANDARD"},
            {"seatNumber":1, "section":"c", "type":"VIP"},
            {"seatNumber":2, "section":"c", "type":"STANDARD"},
            {"seatNumber":3, "section":"c", "type":"STANDARD"},
            {"seatNumber":4, "section":"c", "type":"STANDARD"},
            {"seatNumber":1, "section":"d", "type":"VIP"},
            {"seatNumber":2, "section":"d", "type":"STANDARD"},
            {"seatNumber":3, "section":"d", "type":"STANDARD"},
            {"seatNumber":4, "section":"d", "type":"STANDARD"},
            {"seatNumber":1, "section":"e", "type":"VIP"},
            {"seatNumber":2, "section":"e", "type":"STANDARD"},
            {"seatNumber":3, "section":"e", "type":"STANDARD"},
            {"seatNumber":4, "section":"e", "type":"STANDARD"}
            ],
      "unavailableDateList" : [], "serviceAddress" : ""
    }'
    createHall "$inputHall"

local inputPerformance

inputPerformance='{"performanceId" : 1, "title" : "title",
  "pricePolicies":[
    {"seatType":"VIP","price":14000},
    {"seatType":"STANDARD","price":10000}
  ],
  "bookingStartDate":"2025-05-05 11:00:00",
  "bookingEndDate":"2025-05-07 11:00:00",
  "scheduleList":[
    {"hallId":1, "performanceDate":"2025-06-10 15:00:00"},
    {"hallId":1, "performanceDate":"2025-06-11 15:00:00"}
    ]
  }'

  createPerformance "$inputPerformance"
}

function createHall() {
  local input=$1
  curl -X POST http://$HOST:$PORT/composite/hall -H "Content-Type: application/json" --data "$input"
}

function createPerformance() {
  local input=$1
  curl -X POST http://$HOST:$PORT/composite/performance -H "Content-Type: application/json" --data "$input"
}


function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result="$(eval $curlCmd)"
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"


  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

function testUrl() {
  url=$@
  if curl $url -ks -f -o /dev/null
  then
      echo "Ok"
      return 0
  else
      echo -n "not yet"
      return 1
  fi;
}

function clean() {
  echo "Cleaning DB..."

  # 처음 10초 대기
  sleep 10

  local maxRetries=30
  local retryInterval=3
  local count=0

  while true; do
    response=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE http://$HOST:$PORT/composite/clean-up)

    if [[ "$response" == "200" || "$response" == "204" ]]; then
      echo "Success clean DB.... (HTTP $response)"
      break
    fi

    count=$((count + 1))
    if [[ $count -ge $maxRetries ]]; then
      echo "Failed to clean DB.... HTTP $response after $count retries"
      exit 1
    fi

    echo "Retry cleaning... ($count/$maxRetries), HTTP $response"
    sleep $retryInterval
  done
}

function waitForService() {
  local url=$@
  echo -n "Wait for: $url... "
  local n=0
  until testUrl $url; do
    n=$((n + 1))
    if [[ $n -eq 100 ]]; then
      echo " Give up"
      exit 1
    else
      sleep 6
      echo -n ", retry #$n "
    fi
  done
}

set -e

echo "Start:" `date`

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

waitForService curl -X GET http://$HOST:$PORT/composite/test
clean

setUpTestdata

# Verify that a normal request works, expect three recommendations and three reviews
assertCurl 200 "curl http://$HOST:$PORT/composite/performance-seat/1 -s"
assertEqual 1 $(echo $RESPONSE | jq ".scheduleSummaryWithSeatsList[0].hallId")
assertEqual 20 $(echo $RESPONSE | jq ".scheduleSummaryWithSeatsList[0].performanceSeatList | length")
assertEqual 2 $(echo $RESPONSE | jq ".scheduleSummaryWithSeatsList | length")


if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi

echo "End:" `date`