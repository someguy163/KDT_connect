//js를 통해 리스트와 아작스를 연결
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    /*document.getElementById('startDate').value = new Date().toISOString().substring(0, 10); //현재 날짜로 세팅
    document.getElementById('endDate').value = new Date().toISOString().substring(0, 10); //현재 날짜로 세팅*/
    procurementPlanListAjax(1); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
    orderChoiceAjax(0, 0,0);
    orderListAjax(1, 0); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
    document.getElementById('orderDate2').textContent = new Date().toISOString().substring(0, 10);

});

function procurementPlanListAjax(page) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#procurementPlanList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form1"); //part1_insert_company.html의 form1과연결
    f.page.value = page;

    $.ajax({
        url: "/part2/procurementPlanListAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,
        //성공 시에 part1_insert_company.html의 form태그 위치에 백엔드 경로(part1_insert_company_ajax)에 연결 된 리턴 값인 프론트 cartListAjax.html을 넣어서 보여줌
        success: function (data) {
            $(innerHtml).html(data)
            $("table tr").not(this).removeClass('table-info'); // 배경색 스타일 준 class 전체 제거
            orderChoiceAjax(0, 0,0);
            orderListAjax(1, 0); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function orderChoiceAjax(planNum, index,planCount) {
    const innerHtml = $("#orderRegister");
    const f = document.getElementById("form1");
    f.planNum.value = planNum;

    $.ajax({
        url: "/part2/orderChoiceAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data); // 발주폼 뿌리기
            $("table tr").not(this).removeClass('table-info'); // 배경색 스타일 준 class 전체 제거
            if (index > 0) {
                document.getElementById('index' + index).className += "table-info";// 배경색 스타일 선택 누른부분만 class 추가
            }
            document.getElementById('orderDate2').textContent = new Date().toISOString().substring(0, 10); // 발주일 셋팅
            document.getElementById('receiveDueDate').value = new Date().toISOString().substring(0, 10);
            updateTotalPrice(planCount); // 가격계산 메소드 실행
            orderListAjax(1, planNum);
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function saveOrder() {
    const innerHtml = $("#orderRegister");
    document.getElementById('orderDate').value = new Date().toISOString().substring(0, 10);
    const f = document.getElementById("form2");
    $.ajax({
        url: "/part2/saveOrder", //백엔드 경로
        type: 'POST',
        cache: false,
        data: $('#form2').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data); // 발주폼 뿌리기
            procurementPlanListAjax(1);
            // $("table tr").not(this).removeClass('table-info'); // 배경색 스타일 준 class 전체 제거
            // document.getElementById('orderDate2').textContent = new Date().toISOString().substring(0, 10); // 발주일 셋팅
            // document.getElementById('receiveDueDate').value = new Date().toISOString().substring(0, 10);
            // // updateTotalPrice(0); // 가격계산 메소드 실행/
            // orderListAjax(1, f.planNum.value);
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function orderListAjax(page, planNum) {
    const innerHtml = $("#orderListForm"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form3");
    f.page.value = page;
    if (planNum !== -1) { // -1이 아니면 매개변수로 보내준 planNum 으루 넘어가게 , -1이면 히든으로 채워진 planNum이 넘어가게
        f.planNum.value = planNum;
    }
    $.ajax({
        url: "/part2/orderListAjax",
        type: 'GET',
        cache: false,
        data: $('#form3').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

//마감 펑션
function orderDeadlineAjax(page, planNum, orderNum) {
    const innerHtml = $("#orderListForm"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form3");
    f.page.value = page;
    f.orderNum.value = orderNum;
    if (planNum !== -1) { // -1이 아니면 매개변수로 보내준 planNum 으루 넘어가게 , -1이면 히든으로 채워진 planNum이 넘어가게
        f.planNum.value = planNum;
    }
    $.ajax({
        url: "/part2/orderDeadlineAjax",
        type: 'POST',
        cache: false,
        data: $('#form3').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

//삭제 펑션
function deleteOrderAjax(page, planNum, orderNum) {
    const innerHtml = $("#orderListForm"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form3");
    f.page.value = page;
    f.orderNum.value = orderNum;
    if (planNum !== -1) { // -1이 아니면 매개변수로 보내준 planNum 으루 넘어가게 , -1이면 히든으로 채워진 planNum이 넘어가게
        f.planNum.value = planNum;
    }
    $.ajax({
        url: "/part2/deleteOrderAjax",
        type: 'POST',
        cache: false,
        data: $('#form3').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data)
            procurementPlanListAjax(1);
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

    if (startDate <= endDate) {
        procurementPlanListAjax(1);
    } else if (startDate > endDate) {
        alert("종료날짜를 시작날짜보다 크게 입력하세요");
        $("#endDate").focus();
    } else {
        alert("날짜를 입력하세요");
        $("#startDate").focus();
    }
}

//날짜 계산
function caldate2() {
    var startDate = document.getElementById("startDate3").value;
    var endDate = document.getElementById("endDate3").value;
    const f = document.getElementById("form3");
    if (startDate <= endDate) {
        orderListAjax(1, f.planNum.value)
    } else if (startDate > endDate) {
        alert("종료날짜를 시작날짜보다 크게 입력하세요");
        $("#endDate").focus();
    } else {
        alert("날짜를 입력하세요");
        $("#startDate").focus();
    }
}


//가격 계산
function updateTotalPrice(planCount) {
    // 단가가격
    const onePrice = document.getElementById('onePrice').value;
    // 수량
    if(planCount === -1){
        planCount = document.getElementById('orderCount').value;
    }

    // 단가 * 수량 계산 및 합계/공급가격 금액 형식 추가
    document.getElementById('totalPrice1').textContent = (onePrice * planCount).toLocaleString("ko-KR") + '원';
    document.getElementById('totalPrice2').textContent = (onePrice * planCount).toLocaleString("ko-KR") + '원';
    // 단가 금액 형식 추가
    document.getElementById('orderPrice').textContent = (onePrice * 1).toLocaleString("ko-KR") + '원';

}

// 모달 열기
function printForm(orderNum) {
    // 선택된 행에 대한 tr 요소 가져오기
    // var idx = document.getElementById('index' + orderNum);

    console.log("orderNum : ", orderNum)
    var selectedRow = document.getElementById('orderIndex' + orderNum).closest('tr');
    console.log("selectedRow : ", selectedRow)

    // 선택된 행의 데이터 가져오기
    var itemCode = selectedRow.querySelector('td:nth-child(3)').textContent;
    var itemName = selectedRow.querySelector('td:nth-child(4)').textContent;
    var orderCount = selectedRow.querySelector('td:nth-child(5)').textContent;
    var orderDate = selectedRow.querySelector('td:nth-child(6)').textContent;
    var receiveDueDate = selectedRow.querySelector('td:nth-child(7)').textContent;

    // 숨겨진 입력 필드에서 값 가져오기
    var comName = selectedRow.querySelector('.comName').value;
    var comAdd = selectedRow.querySelector('.comAdd').value;
    var businessId = selectedRow.querySelector('.businessId').value;
    var comManager = selectedRow.querySelector('.comManager').value;
    var itemWidth = selectedRow.querySelector('.itemWidth').value;
    var itemLength = selectedRow.querySelector('.itemLength').value;
    var itemHeight = selectedRow.querySelector('.itemHeight').value;
    var itemMaterial = selectedRow.querySelector('.itemMaterial').value;
    var contractPrice = selectedRow.querySelector('.contractPrice').value;
    var orderInfo = selectedRow.querySelector('.orderInfo').value;
    // totalPrice 추가
    var totalPrice = parseInt(orderCount) * parseInt(contractPrice);
    var itemSize = itemWidth + '*' + itemLength + '*' + itemHeight;

    // 모달폼에 위에서 만든 변수 집어넣기
    // document.getElementById('orderNum').textContent = orderNum;
    document.getElementById('modalOrderDate').textContent = orderDate;
    document.getElementById('modalComName').textContent = comName;
    document.getElementById('modalBusinessId').textContent = businessId;
    document.getElementById('modalComAdd').textContent = comAdd;
    document.getElementById('modalComManager').textContent = comManager;
    document.getElementById('modalItemCode').textContent = itemCode;
    document.getElementById('modalItemName').textContent = itemName;
    document.getElementById('modalItemSize').textContent = itemSize;
    document.getElementById('modalItemMaterial').textContent = itemMaterial;
    document.getElementById('modalOrderCount').textContent = orderCount;
    document.getElementById('modalContractPrice').textContent = contractPrice;
    document.getElementById('modalTotalPrice').textContent = totalPrice;
    document.getElementById('modalReceiveDueDate').textContent = receiveDueDate;
    document.getElementById('modalOrderInfo').textContent = orderInfo;
    document.getElementById('modalTotalPrice2').textContent = totalPrice;

    // 모달 열기
    document.getElementById('myModal').style.display = 'block';
}


// 모달 닫기
function closeModal() {
    document.getElementById('myModal').style.display = 'none';
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    if (event.target == document.getElementById('myModal')) {
        closeModal();
    }
}

// PDF로 저장하기
function savePDF() {
    // 다운로드할 특정 영역 지정
    var element = document.getElementById('modal-content');

    html2canvas(element).then(function (canvas) {
        var imgData = canvas.toDataURL('image/png');
        const {jsPDF} = window.jspdf;
        var pdf = new jsPDF('p', 'mm', 'a4');

        var margin = 10; // 여백 크기 (단위: mm)
        var pageWidth = pdf.internal.pageSize.getWidth();
        var pageHeight = pdf.internal.pageSize.getHeight();
        var imgWidth = pageWidth - 2 * margin; // 페이지 너비 - 2 * 여백
        var imgHeight = canvas.height * imgWidth / canvas.width; // 원본 이미지 비율을 유지하기 위해 이미지 높이를 계산

        // 이미지 높이가 페이지 높이를 초과할 경우
        if (imgHeight > pageHeight - 2 * margin) {
            imgHeight = pageHeight - 2 * margin; // 이미지를 페이지 높이에 맞춰 조정
            imgWidth = canvas.width * imgHeight / canvas.height;
        }

        // pdf 이미지 생성
        pdf.addImage(imgData, 'PNG', margin, margin, imgWidth, imgHeight);

        // 파일명에 현재시간 추가
        const isoString = new Date().toISOString();
        const dateObject = new Date(isoString);
        const year = dateObject.getFullYear();
        const month = String(dateObject.getMonth() + 1).padStart(2, '0');
        const day = String(dateObject.getDate()).padStart(2, '0');
        const hours = String(dateObject.getHours()).padStart(2, '0');
        const minutes = String(dateObject.getMinutes()).padStart(2, '0');
        const seconds = String(dateObject.getSeconds()).padStart(2, '0');
        const formattedDate = `${year}${month}${day}${hours}${minutes}${seconds}`;

        // 파일명에 아이템코드 넣을려고
        var itemCode = document.getElementById('modalItemCode').textContent;

        // 합쳐진 파일명으로 다운로드
        pdf.save(`${formattedDate}_${itemCode}_발주서.pdf`);
    }).catch(function (error) {
        console.error("Error generating PDF: ", error);
    });
}


//프린트하기
function printStart() {
    var g_oBeforeBody = document.getElementById('modal-content').innerHTML;

    // 프린트를 보이는 그대로 나오기위한 셋팅
    window.onbeforeprint = function (ev) {
        document.body.innerHTML = g_oBeforeBody;
    }

    window.print();
    location.reload();

}

