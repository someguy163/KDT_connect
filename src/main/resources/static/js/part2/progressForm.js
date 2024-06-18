//js를 통해 리스트와 아작스를 연결
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    progressScheduleListAjax(1, 0);
    progressChoiceAjax(0, 0);
    progressListAjax(0);
});


//검수 예정 품목 아작스
function progressScheduleListAjax(page, pagingYn) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#progressScheduleList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form1");
    f.page.value = page;

    $.ajax({
        url: "/part2/progressScheduleAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,

        success: function (data) {
            $(innerHtml).html(data)
            if (pagingYn === 1) {
                $("table tr").not(this).removeClass('table-info'); // 배경색 스타일 준 class 전체 제거
                progressChoiceAjax(0, 0);
                progressListAjax(0);
            }
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}


function progressChoiceAjax(orderNum, index) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#progressChoice"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form1");
    f.orderNum.value = orderNum;

    $.ajax({
        url: "/part2/progressChoiceAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,

        success: function (data) {
            $(innerHtml).html(data)
            $("table tr").not(this).removeClass('table-info'); // 배경색 스타일 준 class 전체 제거
            if (index > 0) {
                document.getElementById('index' + index).className += "table-info";// 배경색 스타일 선택 누른부분만 class 추가
            }
            progressListAjax(-1);
            // document.getElementById('progressDate').value = new Date().toISOString().substring(0, 10);
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function saveProgress() {
    const innerHtml = $("#progressChoice");
    document.getElementById('orderDate').value = new Date().toISOString().substring(0, 10);
    const f = document.getElementById("form2");
    const f1 = document.getElementById("form1");
    // Check if the form is valid
    if (!f.checkValidity()) {
        alert('필수값을 전부 입력해주세요');
        return;
    }
    $.ajax({
        url: "/part2/saveProgress", //백엔드 경로
        type: 'POST',
        cache: false,
        data: $('#form2').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data); // 발주폼 뿌리기
            // 검수리스트 아작스 호출
            progressListAjax(-1);

            // 검수예정품목 아작스 호출 (검수현황부분 업데이트 하려고)
            progressScheduleListAjax(f1.page.value,0);
            // 검수버튼 누른상태 유지하기 (한줄 색칠된거 그대로 유지하려고 추가)
            const f = document.getElementById("form1");
            console.log('orderNum : ', f.orderNum.value);
            document.getElementById('index' + f.orderNum.value).className += "table-info";// 배경색 스타일 선택 누른부분만 class 추가
            // document.getElementById('progressDate').value = new Date().toISOString().substring(0, 10);

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function progressListAjax(orderNum) {
    const innerHtml = $("#progressList");
    const f = document.getElementById("form2");
    if (orderNum !== -1) { // -1이 아니면 매개변수로 보내준 orderNum 으루 넘어가게 , -1이면 히든으로 채워진 orderNum 넘어가게
        f.orderNum.value = orderNum;
    }
    $.ajax({
        url: "/part2/progressListAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form2').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data); // 발주폼 뿌리기

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}


//날짜 계산
function caldate() {
    var startDate = document.getElementById("startDate").value;
    var endDate = document.getElementById("endDate").value;
    const f1 = document.getElementById("form1");
    if (startDate <= endDate) {
        progressScheduleListAjax(f1.page.value,0);
    } else if (startDate > endDate) {
        alert("종료날짜를 시작날짜보다 크게 입력하세요");
        $("#endDate").focus();
    } else {
        alert("날짜를 입력하세요");
        $("#startDate").focus();
    }
}

//날짜 계산
function progressDateCheck() {
    var orderDate = document.getElementById("orderDate22").textContent;
    var receiveDueDate = document.getElementById("receiveDueDate").textContent;
    console.log("orderDate : ",orderDate)
    console.log("receiveDueDate : ",receiveDueDate)
    const f2 = document.getElementById("form2");
    console.log("progressDate : ",f2.progressDate.value)
    if (orderDate > f2.progressDate.value) {
        alert("발주일자 이후로 선택해주세요.");
        $("#progressDate").val("");
        $("#progressDate").focus();
    } else if (receiveDueDate < f2.progressDate.value) {
        alert("입고예정일 이전으로 선택해주세요.");
        $("#progressDate").val("");
        $("#progressDate").focus();
    }
}


//수량 제한 걸기
function limitNumber(progressNum) {
    console.log("p : ",progressNum)
    var progressAmount = parseInt(document.getElementById("amount"+progressNum).value, 10);
    var remaining = parseInt(document.getElementById("remaining").value, 10);

    console.log(progressAmount)
    console.log(remaining)
    if (progressAmount > remaining) {
        alert("발주 수량을 넘길수 없습니다.");
        document.getElementById("amount"+progressNum).value="";
        document.getElementById("amount"+progressNum).focus();
    }
}

//검수 리스트 삭제 펑션
function deleteProgressAjax(progressNum, orderNum) {
    const innerHtml = $("#progressList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form3");
    const f1 = document.getElementById("form1");
    f.progressNum.value = progressNum;
    f.orderNum.value = orderNum;
    $.ajax({
        url: "/part2/deleteProgressAjax",
        type: 'GET',
        cache: false,
        data: $('#form3').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)

            // 첫번째 폼
            progressScheduleListAjax(f1.page.value,0);
            // 두번째폼
            progressChoiceAjax(orderNum, orderNum);


            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

//검수 처리 저장
function updateProgress(progressNum, orderNum) {
    const innerHtml = $("#progressList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form3");
    const f1 = document.getElementById("form1");
    f.progressNum.value = progressNum;
    f.progressAmount.value = document.getElementById("amount"+progressNum).value;
    f.progressResult.value = document.getElementById("result"+progressNum).value;
    f.orderNum.value = orderNum;
    $.ajax({
        url: "/part2/updateProgress",
        type: 'POST',
        cache: false,
        data: $('#form3').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)

            // 첫번째 폼
            progressScheduleListAjax(f1.page.value,0);
            // 두번째폼
            progressChoiceAjax(orderNum, orderNum);


            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}