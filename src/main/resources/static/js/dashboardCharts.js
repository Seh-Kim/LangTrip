/*!
대시보드 통계를 표시하기 위해 직접 넣은 js
 */
// dashboardCharts.js
let studyChartInstance = null;
let quizChartInstance = null;

function drawCharts() {
  const studyCtx = document.getElementById('studyChart');
  if (studyCtx) {
    if (studyChartInstance) studyChartInstance.destroy(); // 🔥 기존 차트 제거
    studyChartInstance = new Chart(studyCtx, {
      type: 'line',
      data: {
        labels: ['07-01', '07-02', '07-03', '07-04', '07-05', '07-06', '07-07', '07-08', '07-09', '07-10', '07-11', '07-12', '07-13', '07-14', '07-15', '07-16', '07-17', '07-18', '07-19', '07-20', '07-21', '07-22', '07-23', '07-24', '07-25'],
        datasets: [{
          label: '학습 시간 (분)',
          data: [4,9,7,11,7,8,0,6,9,0,0,0,10,11,9,12,23,9,10,12,27,0,13,17,20,34],
          borderColor: '#3f51b5',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function(tooltipItem) {
                return tooltipItem.raw + ' 분'; // 툴팁에 텍스트 포맷 추가
              }
            }
          }
        },
        scales: {
          x: {
            ticks: {
              font: {
                size: 10,  // X축 레이블 글자 크기 조정
                family: 'Arial, sans-serif',  // 폰트 스타일 변경
                weight: 'normal',
                lineHeight: 1.5
              },
              padding: 10  // X축 레이블과 그래프 간격 설정
            }
          },
          y: {
            ticks: {
              font: {
                size: 12,
                family: 'Arial, sans-serif',
                weight: 'normal',
                lineHeight: 1.5
              },
              padding: 10  // Y축 레이블과 그래프 간격 설정
            }
          }
        }
      }
    });
  }

  const quizCtx = document.getElementById('quizChart');
  if (quizCtx) {
    if (quizChartInstance) quizChartInstance.destroy();
    quizChartInstance = new Chart(quizCtx, {
      type: 'bar',
      data: {
        labels: ['정답', '오답'],
        datasets: [{
          data: [95.5, 4.5],
          backgroundColor: ['#4caf50', '#f44336']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: {
            ticks: {
              font: {
                size: 14,  // X축 레이블 글자 크기 조정
                family: 'Arial, sans-serif',
                weight: 'normal'
              }
            }
          },
          y: {
            ticks: {
              font: {
                size: 14,  // Y축 레이블 글자 크기 조정
                family: 'Arial, sans-serif',
                weight: 'normal'
              }
            }
          }
        }
      }
    });
  }
}

window.drawCharts = drawCharts;
