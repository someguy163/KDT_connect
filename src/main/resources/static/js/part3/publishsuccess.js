//거래명세서 작성 폼
function successModal() {
    // 선택된 라디오 버튼 가져오기
    var selectedRadioButton = document.querySelector('input[name="invoiceNumber"]:checked');

    if (!selectedRadioButton) {
        alert('선택된 항목이 없습니다.');
        return;
    }

    // 선택된 라디오 버튼의 부모 tr 요소 가져오기
    var selectedRow = selectedRadioButton.closest('tr');

    // 선택된 행의 데이터 가져오기
    var invoiceDate = selectedRow.querySelector('td:nth-child(2)').textContent;
    var orderNum = selectedRow.querySelector('td:nth-child(3)').textContent;
    var comName = selectedRow.querySelector('td:nth-child(4)').textContent;
    var itemIndex = selectedRow.querySelector('td:nth-child(5)').textContent;
    var itemName = selectedRow.querySelector('td:nth-child(6)').textContent;
    var orderCount = selectedRow.querySelector('td:nth-child(7)').textContent;
    var publisher = selectedRow.querySelector('td:nth-child(8)').textContent;

    // // 숨겨진 입력 필드에서 값 가져오기
    // var comAdd = document.getElementById('comAdd').value;
    // var businessId = document.getElementById('businessId').value;
    // var comManager = document.getElementById('comManager').value;

    var invoiceNumber = selectedRow.querySelector('.invoiceNumber').value;
    var orderDate = selectedRow.querySelector('.orderDate').value;
    var comAdd = selectedRow.querySelector('.comAdd').value;
    var businessId = selectedRow.querySelector('.businessId').value;
    var comManager = selectedRow.querySelector('.comManager').value;
    var contractPrice = selectedRow.querySelector('.contractPrice').value;
    var invoiceMemo = selectedRow.querySelector('.invoiceMemo').value;

    // totalPrice 추가
    var totalPrice = parseInt(orderCount) * parseInt(contractPrice);

    // 거래명세서 폼에 데이터 출력하기
    document.getElementById('invoiceDate').textContent = invoiceDate;
    document.getElementById('orderNum').textContent = orderNum;
    document.getElementById('comName').textContent = comName;
    document.getElementById('itemIndex').textContent = itemIndex;
    document.getElementById('itemName').textContent = itemName;
    document.getElementById('orderCount').textContent = orderCount;
    document.getElementById('publisher').textContent = publisher;

    // 숨겨진 입력 필드에서 값 가져오기
    // document.getElementById('invoiceNumber').textContent = invoiceNumber;
    // document.getElementById('orderDate').textContent = orderDate;
    // document.getElementById('comAdd').textContent = comAdd;
    // document.getElementById('comManager').textContent = comManager;
    // document.getElementById('businessId').textContent = businessId;
    // document.getElementById('contractPrice').textContent = contractPrice;
    // document.getElementById('invoiceMemo').textContent = invoiceMemo;

    document.getElementById('hiddenInvoiceNumber').value = invoiceNumber;
    document.getElementById('hiddenOrderDate').value = orderDate;
    document.getElementById('hiddenComAdd').value = comAdd;
    document.getElementById('hiddenBusinessId').value = businessId;
    document.getElementById('hiddenComManager').value = comManager;
    document.getElementById('hiddenContractPrice').value = contractPrice;
    document.getElementById('hiddenInvoiceMemo').value = invoiceMemo;

    // 총액
    document.getElementById('totalPrice').textContent = totalPrice;

    // 모달 열기
    document.getElementById('myModal2').style.display = 'block';


}

// 모달 닫기
function closeModal() {
    document.getElementById('myModal2').style.display = 'none';
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    if (event.target == document.getElementById('myModal2')) {
        closeModal();
    }
}

// PDF로 저장하기
function saveAsPDF() {
    var element = document.getElementById('myModal2');

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




// // 저장 버튼 클릭 시
// document.getElementById('saveSuccess').addEventListener('click', function() {
//     // 모달 닫기
//     closeModal();
//
//     // 성공 메시지
//     var successMessage = "[[${successMessage}]]";
//     console.log("Success message: ", successMessage); // 디버깅 메시지 추가
//     if (successMessage) {
//         alert(successMessage);
//     }
// });


