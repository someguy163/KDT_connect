//거래명세서 작성 폼
let invoiceCounter = 1;
function handlePublishButtonClick() {
    // 선택된 라디오 버튼 가져오기
    var selectedRadioButton = document.querySelector('input[name="receiveNum"]:checked');

    if (!selectedRadioButton) {
        alert('선택된 항목이 없습니다.');
        return;
    }

    // 선택된 라디오 버튼의 부모 tr 요소 가져오기
    var selectedRow = selectedRadioButton.closest('tr');

    // 선택된 행의 데이터 가져오기
    var receiveDate = selectedRow.querySelector('td:nth-child(2)').textContent;
    var orderNum = selectedRow.querySelector('td:nth-child(3)').textContent;
    var comName = selectedRow.querySelector('td:nth-child(4)').textContent;
    var itemCode = selectedRow.querySelector('td:nth-child(5)').textContent;
    var itemName = selectedRow.querySelector('td:nth-child(6)').textContent;
    var orderDate = selectedRow.querySelector('td:nth-child(7)').textContent;
    var orderCount = selectedRow.querySelector('td:nth-child(8)').textContent;
    var contractPrice = selectedRow.querySelector('td:nth-child(10)').textContent;

    // // 숨겨진 입력 필드에서 값 가져오기
    // var comAdd = document.getElementById('comAdd').value;
    // var businessId = document.getElementById('businessId').value;
    // var comManager = document.getElementById('comManager').value;

    var receiveNum = selectedRow.querySelector('.receiveNum').value;
    var comAdd = selectedRow.querySelector('.comAdd').value;
    var businessId = selectedRow.querySelector('.businessId').value;
    var comManager = selectedRow.querySelector('.comManager').value;

    // // 숨겨진 입력 필드에서 값 가져오기
    // var comAdd = selectedRow.querySelector('input[type="hidden"][name="comAdd"]').value;
    // var businessId = selectedRow.querySelector('input[type="hidden"][name="businessId"]').value;
    // var comManager = selectedRow.querySelector('input[type="hidden"][name="comManager"]').value;

    // totalPrice 추가
    var totalPrice = parseInt(orderCount) * parseInt(contractPrice);

    // 거래명세서 폼에 데이터 출력하기
    document.getElementById('receiveDate').textContent = receiveDate;
    document.getElementById('orderNum').textContent = orderNum;
    document.getElementById('comName').textContent = comName;
    document.getElementById('itemCode').textContent = itemCode;
    document.getElementById('itemName').textContent = itemName;
    document.getElementById('orderDate').textContent = orderDate;
    document.getElementById('orderCount').textContent = orderCount;
    document.getElementById('contractPrice').textContent = contractPrice;


    // 숨겨진 입력 필드에서 값 가져오기
    document.getElementById('comAdd').textContent = comAdd;
    document.getElementById('comManager').textContent = comManager;
    document.getElementById('businessId').textContent = businessId;
    document.getElementById('receiveNum').textContent = receiveNum;

    document.getElementById('hiddenReceiveNum').value = receiveNum;
    document.getElementById('hiddenComAdd').value = comAdd;
    document.getElementById('hiddenBusinessId').value = businessId;
    document.getElementById('hiddenComManager').value = comManager;

    // 총액
    document.getElementById('totalPrice').textContent = totalPrice;

    // 거래명세서 번호 출력하기 - 저장이될때마다 카운트 1씩 증가
    document.getElementById('invoiceNumber').innerText = invoiceCounter++;

    // 모달 열기
    document.getElementById('myModal').style.display = 'block';

    // 발행된 거래명세서 품목 숨기기
    selectedRow.style.display = 'none';

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
function saveAsPDF() {
    var element = document.getElementById('myModal');

    html2canvas(element).then(canvas => {
        var imgData = canvas.toDataURL('image/png');
        var pdf = new jsPDF('p', 'mm', 'a4');
        var imgWidth = 210;
        var pageHeight = 295;
        var imgHeight = canvas.height * imgWidth / canvas.width;
        var heightLeft = imgHeight;

        var position = 0;

        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;

        while (heightLeft >= 0) {
            position = heightLeft - imgHeight;
            pdf.addPage();
            pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
            heightLeft -= pageHeight;
        }

        pdf.save('document.pdf');
    });
}




