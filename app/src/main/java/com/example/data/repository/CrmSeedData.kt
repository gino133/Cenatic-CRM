package com.example.data.repository

import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceType
import com.example.data.model.CustomerEntity
import com.example.data.model.CustomerStatus
import com.example.data.model.DealEntity
import com.example.data.model.DealStage
import com.example.data.model.EmployeeItem
import com.example.data.model.InteractionEntity
import com.example.data.model.InteractionType
import com.example.data.model.OvertimeRateType
import com.example.data.model.ProjectProgressItem
import com.example.data.model.ProjectStatusType
import com.example.data.model.ProjectStep
import com.example.data.model.QuoteItem
import com.example.data.model.QuoteRevision
import com.example.data.model.StepStatus
import com.example.data.model.TaskEntity
import com.example.data.model.TaskPriority
import com.example.data.model.TaskType
import java.util.Calendar

object CrmSeedData {

    fun timeFor(year: Int, month: Int, day: Int, hour: Int = 10, minute: Int = 0): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getSampleCustomers(): List<CustomerEntity> {
        return listOf(
            CustomerEntity(
                id = 1,
                name = "Nguyễn Văn A",
                company = "Công ty CP Công Nghệ TechVN",
                position = "Giám đốc Công nghệ",
                phone = "0988 123 456",
                email = "nguyenvana@techvn.com",
                address = "Tòa nhà TechVN, Duy Tân, Cầu Giấy, Hà Nội",
                status = CustomerStatus.VIP.name,
                source = "Website",
                tags = "Phần mềm, Cloud, Trọng điểm",
                estimatedValue = 480_000_000.0,
                progressPercent = 90,
                isCorporate = true,
                taxCode = "0108998877",
                contactPerson = "Nguyễn Văn A - 0988 123 456",
                notes = "Khách hàng VIP chiến lược, đã triển khai hệ thống ERP và Cloud Infrastructure.",
                avatarColorHex = "#2563EB",
                createdAt = timeFor(2025, 12, 1),
                updatedAt = timeFor(2026, 8, 28)
            ),
            CustomerEntity(
                id = 2,
                name = "Trần Thị Bích",
                company = "Tập đoàn Xây dựng Hưng Phát",
                position = "Tổng Giám Đốc",
                phone = "0912 345 678",
                email = "bich.tran@hungphatgroup.vn",
                address = "128 Nguyễn Đình Chiểu, Quận 3, TP. Hồ Chí Minh",
                status = CustomerStatus.CLOSED.name,
                source = "Giới thiệu",
                tags = "Xây dựng, Hợp đồng lớn, Đã chốt",
                estimatedValue = 650_000_000.0,
                progressPercent = 100,
                isCorporate = true,
                taxCode = "0312345678",
                contactPerson = "Trần Thị Bích - 0912 345 678",
                notes = "Đã ký kết hợp đồng thi công và triển khai giải pháp quản lý tiến độ công trình toàn diện.",
                avatarColorHex = "#10B981",
                createdAt = timeFor(2025, 12, 10),
                updatedAt = timeFor(2026, 8, 25)
            ),
            CustomerEntity(
                id = 3,
                name = "Lê Hoàng Cường",
                company = "Công ty TNHH Thương Mại Toàn Cầu",
                position = "Trưởng phòng Kinh doanh",
                phone = "0977 654 321",
                email = "cuong.le@toancau.vn",
                address = "Số 45 Lê Thánh Tông, Ngô Quyền, Hải Phòng",
                status = CustomerStatus.CUSTOMER.name,
                source = "Sự kiện",
                tags = "Thương mại, Xuất nhập khẩu",
                estimatedValue = 320_000_000.0,
                progressPercent = 80,
                isCorporate = true,
                taxCode = "0209876543",
                contactPerson = "Lê Hoàng Cường - 0977 654 321",
                notes = "Đã hoàn thành giai đoạn 1, đang thương thảo hợp đồng bảo trì nâng cấp hàng năm.",
                avatarColorHex = "#0EA5E9",
                createdAt = timeFor(2026, 1, 5),
                updatedAt = timeFor(2026, 8, 20)
            ),
            CustomerEntity(
                id = 4,
                name = "Phạm Thị Diệu",
                company = "Dịch vụ Tư vấn Quản trị ABC",
                position = "Phó Giám Đốc",
                phone = "0933 888 999",
                email = "phamthidieu@tuvanabc.vn",
                address = "250 Võ Văn Kiệt, Quận 1, TP. Hồ Chí Minh",
                status = CustomerStatus.LEAD.name,
                source = "Mạng xã hội",
                tags = "Tư vấn tài chính, Tiềm năng",
                estimatedValue = 95_000_000.0,
                progressPercent = 40,
                isCorporate = true,
                taxCode = "0305566778",
                contactPerson = "Phạm Thị Diệu - 0933 888 999",
                notes = "Đang thẩm định giải pháp tự động hóa quy trình chăm sóc khách hàng đa kênh.",
                avatarColorHex = "#64748B",
                createdAt = timeFor(2026, 1, 15),
                updatedAt = timeFor(2026, 8, 15)
            ),
            CustomerEntity(
                id = 5,
                name = "Nguyễn Văn Khang",
                company = "Tập đoàn BĐS Khang Điền",
                position = "Chủ Tịch HĐQT",
                phone = "0901 234 567",
                email = "khang.nguyen@khangdien.vn",
                address = "Số 45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM",
                status = CustomerStatus.VIP.name,
                source = "Đối tác chiến lược",
                tags = "Khách hàng VIP, Bất động sản, Siêu trọng điểm",
                estimatedValue = 1_200_000_000.0,
                progressPercent = 95,
                isCorporate = true,
                taxCode = "0301122334",
                contactPerson = "Nguyễn Văn Khang - 0901 234 567",
                dob = "15/08/1982",
                notes = "Khách hàng VIP trọng yếu với chuỗi dự án căn hộ cao cấp và văn phòng thông minh.",
                avatarColorHex = "#7C3AED",
                createdAt = timeFor(2025, 12, 5),
                updatedAt = timeFor(2026, 8, 29)
            ),
            CustomerEntity(
                id = 6,
                name = "Hoàng Minh Tuấn",
                company = "Chuỗi Bán Lẻ EcoMart Việt Nam",
                position = "Giám đốc Vận hành",
                phone = "0944 556 677",
                email = "tuan.hoang@ecomart.vn",
                address = "Tầng 5, TTTM Vincom Mega Mall, Thảo Điền, TP. Thủ Đức",
                status = CustomerStatus.PARTNER.name,
                source = "Hội thảo bán lẻ",
                tags = "Bán lẻ, Chuỗi cửa hàng, Tích điểm CRM",
                estimatedValue = 520_000_000.0,
                progressPercent = 85,
                isCorporate = true,
                taxCode = "0309988776",
                contactPerson = "Hoàng Minh Tuấn - 0944 556 677",
                notes = "Hợp tác cung cấp giải pháp POS và quản lý hội viên trên 50 siêu thị toàn quốc.",
                avatarColorHex = "#F59E0B",
                createdAt = timeFor(2026, 2, 1),
                updatedAt = timeFor(2026, 8, 24)
            ),
            CustomerEntity(
                id = 7,
                name = "Đặng Thị Phương Thảo",
                company = "Công ty Dược Phẩm An Khang",
                position = "Trưởng Ban Thu Mua",
                phone = "0968 112 233",
                email = "thao.dang@ankhangpharma.com",
                address = "Khu Công Nghiệp Tân Bình, Tân Phú, TP. Hồ Chí Minh",
                status = CustomerStatus.CUSTOMER.name,
                source = "Triển lãm Y Dược",
                tags = "Dược phẩm, Quản lý kho, ISO",
                estimatedValue = 380_000_000.0,
                progressPercent = 75,
                isCorporate = true,
                taxCode = "0314455667",
                contactPerson = "Đặng Thị Phương Thảo - 0968 112 233",
                notes = "Đã vận hành phân hệ quản lý kho GSP và chuẩn bị mở rộng thêm phân hệ DMS phân phối.",
                avatarColorHex = "#EC4899",
                createdAt = timeFor(2026, 2, 20),
                updatedAt = timeFor(2026, 8, 18)
            ),
            CustomerEntity(
                id = 8,
                name = "Vũ Đình Trọng",
                company = "Kiến Trúc & Nội Thất Casa Luxury",
                position = "Giám Đốc Thiết Kế",
                phone = "0971 223 344",
                email = "trong.vu@casaluxury.com.vn",
                address = "Số 188 Pasteur, Phường 6, Quận 3, TP. Hồ Chí Minh",
                status = CustomerStatus.CLOSED.name,
                source = "Website",
                tags = "Nội thất cao cấp, 3D Render, Showroom",
                estimatedValue = 290_000_000.0,
                progressPercent = 100,
                isCorporate = true,
                taxCode = "0318899001",
                contactPerson = "Vũ Đình Trọng - 0971 223 344",
                notes = "Đã hoàn thành thanh toán 100% gói thiết kế và phần mềm quản trị showroom.",
                avatarColorHex = "#8B5CF6",
                createdAt = timeFor(2026, 3, 10),
                updatedAt = timeFor(2026, 8, 12)
            ),
            CustomerEntity(
                id = 9,
                name = "Bùi Thanh Hương",
                company = "Hương Giang Logistics & Vận Tải Biển",
                position = "Tổng Giám Đốc",
                phone = "0938 776 655",
                email = "huong.bui@huonggianglogistics.vn",
                address = "Tòa nhà IPC, 1489 Nguyễn Văn Linh, Quận 7, TP.HCM",
                status = CustomerStatus.VIP.name,
                source = "Đối tác",
                tags = "Logistics, Giao vận quốc tế, VIP",
                estimatedValue = 890_000_000.0,
                progressPercent = 90,
                isCorporate = true,
                taxCode = "0307788990",
                contactPerson = "Bùi Thanh Hương - 0938 776 655",
                notes = "Hợp đồng tích hợp quản trị chuỗi cung ứng và theo dõi định vị container theo thời gian thực.",
                avatarColorHex = "#059669",
                createdAt = timeFor(2026, 3, 25),
                updatedAt = timeFor(2026, 8, 27)
            ),
            CustomerEntity(
                id = 10,
                name = "Lý Gia Bảo",
                company = "Chuỗi Nhà Hàng Golden Lotus",
                position = "Chủ chuỗi nhà hàng",
                phone = "0918 334 455",
                email = "giabao.ly@goldenlotus.vn",
                address = "72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP. Hồ Chí Minh",
                status = CustomerStatus.CUSTOMER.name,
                source = "Giới thiệu",
                tags = "F&B, Nhà hàng, Khách hàng thân thiết",
                estimatedValue = 240_000_000.0,
                progressPercent = 80,
                isCorporate = true,
                taxCode = "0316655443",
                contactPerson = "Lý Gia Bảo - 0918 334 455",
                notes = "Đang sử dụng hệ thống đặt bàn trực tuyến và tích điểm thành viên đa chi nhánh.",
                avatarColorHex = "#D97706",
                createdAt = timeFor(2026, 4, 8),
                updatedAt = timeFor(2026, 8, 14)
            ),
            CustomerEntity(
                id = 11,
                name = "Trịnh Quốc Huy",
                company = "Trường Quốc Tế Gateway Academy",
                position = "Giám Đốc CNTT",
                phone = "0909 554 433",
                email = "huy.trinh@gatewayacademy.edu.vn",
                address = "Khu Đô Thị Sala, TP. Thủ Đức, TP. Hồ Chí Minh",
                status = CustomerStatus.CUSTOMER.name,
                source = "Sự kiện EdTech",
                tags = "Giáo dục, EdTech, Quản lý học sinh",
                estimatedValue = 420_000_000.0,
                progressPercent = 85,
                isCorporate = true,
                taxCode = "0317788112",
                contactPerson = "Trịnh Quốc Huy - 0909 554 433",
                notes = "Cung cấp hệ thống sổ liên lạc điện tử và cổng thanh toán học phí tự động.",
                avatarColorHex = "#3B82F6",
                createdAt = timeFor(2026, 4, 22),
                updatedAt = timeFor(2026, 8, 22)
            ),
            CustomerEntity(
                id = 12,
                name = "Ngô Mỹ Linh",
                company = "Thời Trang Thiết Kế Elise Chic",
                position = "Giám Đốc Thương Hiệu",
                phone = "0982 990 011",
                email = "linh.ngo@elisechic.com",
                address = "95 Hai Bà Trưng, Quận 1, TP. Hồ Chí Minh",
                status = CustomerStatus.CASUAL.name,
                source = "Instagram Ads",
                tags = "Thời trang, Vãng lai, E-commerce",
                estimatedValue = 65_000_000.0,
                progressPercent = 30,
                isCorporate = false,
                contactPerson = "Ngô Mỹ Linh",
                notes = "Quan tâm đến giải pháp quản lý bán hàng Livestream và kết nối sàn TikTok Shop.",
                avatarColorHex = "#E11D48",
                createdAt = timeFor(2026, 5, 12),
                updatedAt = timeFor(2026, 8, 10)
            ),
            CustomerEntity(
                id = 13,
                name = "Đỗ Cao Cường",
                company = "Cơ Khí Chế Tạo & Tự Động Hóa VinaMach",
                position = "Phó Giám Đốc Kỹ Thuật",
                phone = "0915 667 788",
                email = "cuong.do@vinamach.vn",
                address = "Khu Công Nghệ Cao, TP. Thủ Đức, TP. Hồ Chí Minh",
                status = CustomerStatus.LEAD.name,
                source = "Hội thảo Công Nghiệp 4.0",
                tags = "Cơ khí, IoT, Tự động hóa",
                estimatedValue = 350_000_000.0,
                progressPercent = 50,
                isCorporate = true,
                taxCode = "0319988123",
                contactPerson = "Đỗ Cao Cường - 0915 667 788",
                notes = "Đang thẩm định giải pháp thu thập dữ liệu máy móc CNC vào hệ thống dashboard trung tâm.",
                avatarColorHex = "#475569",
                createdAt = timeFor(2026, 6, 2),
                updatedAt = timeFor(2026, 8, 19)
            ),
            CustomerEntity(
                id = 14,
                name = "Phan Thùy Trang",
                company = "Nha Khoa Thẩm Mỹ SunSmile",
                position = "Bác Sĩ Trưởng / Chủ Phòng Khám",
                phone = "0934 112 288",
                email = "trang.phan@sunsmile.vn",
                address = "320 Nguyễn Trãi, Quận 5, TP. Hồ Chí Minh",
                status = CustomerStatus.CUSTOMER.name,
                source = "Khách hàng cũ giới thiệu",
                tags = "Nha khoa, Y tế, Lịch hẹn bác sĩ",
                estimatedValue = 160_000_000.0,
                progressPercent = 80,
                isCorporate = true,
                taxCode = "0315544332",
                contactPerson = "Phan Thùy Trang - 0934 112 288",
                notes = "Triển khai phần mềm quản lý hồ sơ bệnh án nha khoa và nhắc lịch khám tự động qua Zalo ZNS.",
                avatarColorHex = "#0D9488",
                createdAt = timeFor(2026, 6, 20),
                updatedAt = timeFor(2026, 8, 26)
            ),
            CustomerEntity(
                id = 15,
                name = "Vương Đình Hải",
                company = "Cá nhân - Nhà Đầu Tư BĐS Tự Do",
                position = "Nhà đầu tư cá nhân",
                phone = "0908 998 877",
                email = "hai.vuong@gmail.com",
                address = "Biệt thự Lan Anh, Thảo Điền, TP. Thủ Đức",
                status = CustomerStatus.VIP.name,
                source = "Mối quan hệ riêng",
                tags = "Cá nhân VIP, Đầu tư, Tài chính lớn",
                estimatedValue = 550_000_000.0,
                progressPercent = 90,
                isCorporate = false,
                contactPerson = "Vương Đình Hải",
                notes = "Cá nhân VIP đầu tư chuỗi biệt thự cho thuê, cần hệ thống báo cáo doanh thu & tài sản riêng.",
                avatarColorHex = "#9333EA",
                createdAt = timeFor(2026, 7, 5),
                updatedAt = timeFor(2026, 8, 30)
            )
        )
    }

    fun getSampleDeals(): List<DealEntity> {
        return listOf(
            // === THÁNG 12/2025 === (Doanh thu ~275M)
            DealEntity(
                id = 1,
                customerId = 1,
                title = "Gói Nâng Cấp Máy Chủ & Cloud TechVN Q4/2025",
                value = 180_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2025, 12, 18),
                notes = "Đã bàn giao nghiệm thu đợt 1 và thanh toán 100%.",
                createdAt = timeFor(2025, 12, 2)
            ),
            DealEntity(
                id = 2,
                customerId = 2,
                title = "Khảo Sát & Thiết Kế Module Quản Lý Vật Tư Hưng Phát",
                value = 95_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2025, 12, 26),
                notes = "Nghiệm thu đúng tiến độ trước thềm Tết Dương lịch.",
                createdAt = timeFor(2025, 12, 12)
            ),
            DealEntity(
                id = 3,
                customerId = 4,
                title = "Tư Vấn Chuyển Đổi Số Tổng Thể 2025",
                value = 40_000_000.0,
                stage = DealStage.LOST.name,
                probability = 0,
                expectedCloseDate = timeFor(2025, 12, 28),
                notes = "Khách hàng dời ngân sách sang đầu năm 2026.",
                createdAt = timeFor(2025, 12, 15)
            ),

            // === THÁNG 01/2026 === (Doanh thu ~415M)
            DealEntity(
                id = 4,
                customerId = 5,
                title = "Hợp Đồng Cổng Thông Tin Cư Dân Khang Điền Luxury",
                value = 210_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 1, 15),
                notes = "Ký kết hợp đồng đầu năm mới 2026.",
                createdAt = timeFor(2026, 1, 2)
            ),
            DealEntity(
                id = 5,
                customerId = 3,
                title = "Triển Khai Phần Mềm Bán Hàng Toàn Cầu Q1/2026",
                value = 120_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 1, 22),
                notes = "Đã chốt hợp đồng và tiến hành đào tạo người dùng.",
                createdAt = timeFor(2026, 1, 6)
            ),
            DealEntity(
                id = 6,
                customerId = 4,
                title = "Gói Bản Quyền Bảo Mật Dữ Liệu Doanh Nghiệp ABC",
                value = 85_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 1, 28),
                notes = "Đã kích hoạt License và thanh toán chuyển khoản.",
                createdAt = timeFor(2026, 1, 10)
            ),

            // === THÁNG 02/2026 === (Doanh thu ~250M)
            DealEntity(
                id = 7,
                customerId = 6,
                title = "Hệ Thống Tích Điểm Hội Viên EcoMart Chuỗi 1",
                value = 160_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 2, 14),
                notes = "Chốt hợp đồng triển khai trước Tết Nguyên Đán.",
                createdAt = timeFor(2026, 2, 1)
            ),
            DealEntity(
                id = 8,
                customerId = 7,
                title = "Cấu Hình Quản Lý Kho Dược GSP An Khang",
                value = 90_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 2, 25),
                notes = "Nghiệm thu module kho thành công.",
                createdAt = timeFor(2026, 2, 15)
            ),

            // === THÁNG 03/2026 === (Doanh thu ~430M)
            DealEntity(
                id = 9,
                customerId = 8,
                title = "Hợp Đồng Thiết Kế 3D & Phần Mềm Showroom Casa",
                value = 220_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 3, 16),
                notes = "Khách hàng rất hài lòng về giải pháp VR 3D tương tác.",
                createdAt = timeFor(2026, 3, 2)
            ),
            DealEntity(
                id = 10,
                customerId = 9,
                title = "Module Tích Hợp Định Vị Container Hương Giang",
                value = 130_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 3, 24),
                notes = "Đã kết nối API GPS cho 50 đầu kéo vận tải.",
                createdAt = timeFor(2026, 3, 10)
            ),
            DealEntity(
                id = 11,
                customerId = 1,
                title = "Dịch Vụ Hỗ Trợ 24/7 & Nâng Cấp Đường Truyền TechVN",
                value = 80_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 3, 29),
                notes = "Gia hạn hợp đồng dịch vụ SLA Vàng.",
                createdAt = timeFor(2026, 3, 15)
            ),

            // === THÁNG 04/2026 === (Doanh thu ~580M)
            DealEntity(
                id = 12,
                customerId = 2,
                title = "Gói Triển Khai ERP Thi Công Công Trình Hưng Phát Giai Đoạn 2",
                value = 310_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 4, 15),
                notes = "Ký kết gói mở rộng thêm 5 dự án đô thị mới.",
                createdAt = timeFor(2026, 4, 1)
            ),
            DealEntity(
                id = 13,
                customerId = 10,
                title = "Hệ Thống Đặt Bàn & Thanh Toán Golden Lotus",
                value = 175_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 4, 20),
                notes = "Triển khai trên toàn bộ 6 chi nhánh TP.HCM.",
                createdAt = timeFor(2026, 4, 7)
            ),
            DealEntity(
                id = 14,
                customerId = 11,
                title = "Cổng Thông Tin Học Sinh & Phụ Huynh Gateway",
                value = 95_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 4, 27),
                notes = "Đưa vào thử nghiệm thành công cho 1.200 học sinh.",
                createdAt = timeFor(2026, 4, 12)
            ),

            // === THÁNG 05/2026 === (Doanh thu ~485M)
            DealEntity(
                id = 15,
                customerId = 5,
                title = "Hợp Đồng Bảo Trì & Nâng Cấp Hệ Thống Khang Điền 2026",
                value = 260_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 5, 12),
                notes = "Hợp đồng bảo trì toàn diện 12 tháng.",
                createdAt = timeFor(2026, 5, 2)
            ),
            DealEntity(
                id = 16,
                customerId = 6,
                title = "Mở Rộng Hệ Thống EcoMart Giai Đoạn Miền Trung",
                value = 140_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 5, 22),
                notes = "Thêm 15 chi nhánh Đà Nẵng và Nha Trang.",
                createdAt = timeFor(2026, 5, 8)
            ),
            DealEntity(
                id = 17,
                customerId = 14,
                title = "Phần Mềm Quản Lý Phòng Khám SunSmile Chi Nhánh 1",
                value = 85_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 5, 29),
                notes = "Ký kết và thanh toán đợt 1.",
                createdAt = timeFor(2026, 5, 16)
            ),

            // === THÁNG 06/2026 === (Doanh thu ~620M)
            DealEntity(
                id = 18,
                customerId = 9,
                title = "Hệ Thống Quản Trị Chuỗi Cung Ứng Toàn Diện Hương Giang",
                value = 320_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 6, 18),
                notes = "Hợp đồng lớn nhất trong quý 2.",
                createdAt = timeFor(2026, 6, 1)
            ),
            DealEntity(
                id = 19,
                customerId = 11,
                title = "Gói Thu Phí Tự Động Học Đường & Quản Lý Xe Bus Gateway",
                value = 190_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 6, 25),
                notes = "Tích hợp thẻ RFID và nhận diện khuôn mặt.",
                createdAt = timeFor(2026, 6, 10)
            ),
            DealEntity(
                id = 20,
                customerId = 7,
                title = "Phân Hệ Quản Lý Phân Phối DMS An Khang Pharma",
                value = 110_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 6, 28),
                notes = "Hoàn thành đào tạo 40 trình dược viên.",
                createdAt = timeFor(2026, 6, 14)
            ),

            // === THÁNG 07/2026 === (Doanh thu ~665M)
            DealEntity(
                id = 21,
                customerId = 1,
                title = "Triển Khai Trí Tuệ Nhân Tạo Dự Báo Doanh Thu TechVN",
                value = 250_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 7, 10),
                notes = "Ứng dụng mô hình AI Gemini vào tự động hóa dự báo.",
                createdAt = timeFor(2026, 7, 1)
            ),
            DealEntity(
                id = 22,
                customerId = 15,
                title = "Phần Mềm Quản Trị Danh Mục Bất Động Sản Cao Cấp",
                value = 180_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 7, 17),
                notes = "Cá nhân VIP Vương Đình Hải ký kết hợp đồng.",
                createdAt = timeFor(2026, 7, 5)
            ),
            DealEntity(
                id = 23,
                customerId = 3,
                title = "Hợp Đồng Tích Hợp Sàn Thương Mại Điện Tử Quốc Tế",
                value = 145_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 7, 24),
                notes = "Kết nối Amazon và Alibaba API.",
                createdAt = timeFor(2026, 7, 8)
            ),
            DealEntity(
                id = 24,
                customerId = 8,
                title = "Gia Hạn Bản Quyền Phần Mềm Quản Lý Casa 2026",
                value = 90_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 7, 30),
                notes = "Thanh toán gia hạn năm thứ 2.",
                createdAt = timeFor(2026, 7, 15)
            ),

            // === THÁNG 08/2026 (HIỆN TẠI) === (Doanh thu WON ~750M + Deals Pipeline)
            DealEntity(
                id = 25,
                customerId = 5,
                title = "Hệ Thống Quản Trị Smart City Khu Đô Thị Khang Điền 2026",
                value = 350_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 8, 12),
                notes = "Đã ký kết hợp đồng chính thức, tiến hành kick-off.",
                createdAt = timeFor(2026, 8, 1)
            ),
            DealEntity(
                id = 26,
                customerId = 1,
                title = "Triển Khai ERP Cloud Core 100 Người Dùng",
                value = 280_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 8, 20),
                notes = "Đã thanh toán 100% hợp đồng năm đầu tiên.",
                createdAt = timeFor(2026, 8, 5)
            ),
            DealEntity(
                id = 27,
                customerId = 6,
                title = "Gói Quản Lý Bán Lẻ & Tích Điểm EcoMart 50 Cửa Hàng",
                value = 120_000_000.0,
                stage = DealStage.WON.name,
                probability = 100,
                expectedCloseDate = timeFor(2026, 8, 28),
                notes = "Đã ký hợp đồng và cấu hình máy chủ POS.",
                createdAt = timeFor(2026, 8, 10)
            ),
            // Các deals đang đàm phán trong tháng 8
            DealEntity(
                id = 28,
                customerId = 9,
                title = "Hợp Đồng Mở Rộng API Giao Vận Logistics Toàn Quốc",
                value = 450_000_000.0,
                stage = DealStage.NEGOTIATION.name,
                probability = 75,
                expectedCloseDate = timeFor(2026, 9, 10),
                notes = "Thảo luận chia sẻ doanh thu và cam kết SLA tốc độ xử lý đơn hàng.",
                createdAt = timeFor(2026, 8, 15)
            ),
            DealEntity(
                id = 29,
                customerId = 13,
                title = "Giải Pháp IoT Giám Sát Nhà Máy Cơ Khí VinaMach",
                value = 350_000_000.0,
                stage = DealStage.PROPOSAL.name,
                probability = 40,
                expectedCloseDate = timeFor(2026, 9, 20),
                notes = "Chuẩn bị tài liệu kỹ thuật tham gia vòng thuyết trình giải pháp.",
                createdAt = timeFor(2026, 8, 20)
            ),
            DealEntity(
                id = 30,
                customerId = 12,
                title = "Phần Mềm Quản Lý Bán Hàng & Livestream Elise Chic",
                value = 65_000_000.0,
                stage = DealStage.LEAD.name,
                probability = 20,
                expectedCloseDate = timeFor(2026, 9, 25),
                notes = "Đã gửi catalog giải pháp và video hướng dẫn sử dụng.",
                createdAt = timeFor(2026, 8, 25)
            )
        )
    }

    fun getSampleInteractions(): List<InteractionEntity> {
        return listOf(
            InteractionEntity(
                id = 1,
                customerId = 1,
                type = InteractionType.CALL.name,
                title = "Gọi điện thống nhất lịch họp kick-off dự án ERP",
                content = "Trao đổi với anh Nam về danh sách nhân sự tham gia đội triển khai dự án ERP.",
                date = timeFor(2026, 8, 28, 9, 30),
                outcome = "Anh Nam chốt họp vào sáng thứ 2 tuần tới lúc 9:00 tại trụ sở TechVN.",
                followUpDate = timeFor(2026, 9, 3, 9, 0),
                ratingScore = 5
            ),
            InteractionEntity(
                id = 2,
                customerId = 5,
                type = InteractionType.MEETING.name,
                title = "Họp trực tiếp cùng Chủ tịch Nguyễn Văn Khang",
                content = "Trình bày báo cáo tiến độ Smart City và bản demo ứng dụng cư dân phiên bản mới.",
                date = timeFor(2026, 8, 27, 14, 0),
                outcome = "Chủ tịch đánh giá cao giao diện trực quan và đồng ý nghiệm thu giai đoạn 1.",
                followUpDate = timeFor(2026, 9, 5, 10, 0),
                ratingScore = 5
            ),
            InteractionEntity(
                id = 3,
                customerId = 2,
                type = InteractionType.CONTRACT.name,
                title = "Ký kết phụ lục hợp đồng mở rộng ERP xây dựng",
                content = "Hoàn tất ký số hợp đồng điện tử và nhận tạm ứng 50% giá trị gói mở rộng.",
                date = timeFor(2026, 8, 25, 11, 0),
                outcome = "Thủ tục pháp lý hoàn tất, phòng kế toán đã xuất hóa đơn GTGT.",
                followUpDate = timeFor(2026, 9, 15, 14, 0),
                ratingScore = 5
            ),
            InteractionEntity(
                id = 4,
                customerId = 6,
                type = InteractionType.MESSAGE.name,
                title = "Trao đổi Zalo hỗ trợ cấu hình máy quét mã vạch",
                content = "Gửi file cấu hình driver và tài liệu video hướng dẫn cho kỹ thuật viên EcoMart.",
                date = timeFor(2026, 8, 26, 16, 30),
                outcome = "Kỹ thuật viên phản hồi đã kết nối thành công tại siêu thị Thảo Điền.",
                followUpDate = null,
                ratingScore = 4
            ),
            InteractionEntity(
                id = 5,
                customerId = 9,
                type = InteractionType.EMAIL.name,
                title = "Gửi bảng báo giá chi tiết và cam kết SLA giao vận",
                content = "Email bảng chiết khấu và tài liệu kỹ thuật bảo mật máy chủ cấp doanh nghiệp.",
                date = timeFor(2026, 8, 24, 10, 15),
                outcome = "Chị Thảo xác nhận đã nhận email và chuyển ban kiểm toán thẩm định.",
                followUpDate = timeFor(2026, 9, 2, 9, 0),
                ratingScore = 4
            ),
            InteractionEntity(
                id = 6,
                customerId = 8,
                type = InteractionType.MEETING.name,
                title = "Demo công nghệ VR 3D Render tại Showroom Casa",
                content = "Trải nghiệm kính thực tế ảo VR xem nội thất căn hộ mẫu với ban giám đốc Casa.",
                date = timeFor(2026, 7, 20, 15, 0),
                outcome = "Khách hàng duyệt ký hợp đồng gia hạn.",
                followUpDate = null,
                ratingScore = 5
            ),
            InteractionEntity(
                id = 7,
                customerId = 11,
                type = InteractionType.CALL.name,
                title = "Trao đổi chuẩn bị năm học mới 2026 - 2027",
                content = "Kiểm tra tải hệ thống và cấp phát tài khoản phụ huynh cho khối học sinh mới.",
                date = timeFor(2026, 8, 20, 14, 30),
                outcome = "Hệ thống sẵn sàng phục vụ 2.500 phụ huynh trong lễ khai giảng.",
                followUpDate = timeFor(2026, 9, 5, 8, 0),
                ratingScore = 5
            )
        )
    }

    fun getSampleTasks(): List<TaskEntity> {
        return listOf(
            TaskEntity(
                id = 1,
                customerId = 1,
                title = "Gọi điện tư vấn phân hệ AI Dự báo cho TechVN",
                description = "Trao đổi về các thuật toán tự động nhận diện cơ hội mua thêm trong CRM.",
                dueDate = timeFor(2026, 8, 31, 10, 0),
                priority = TaskPriority.HIGH.name,
                isCompleted = false,
                taskType = TaskType.CALL_BACK.name,
                location = "10:00 AM"
            ),
            TaskEntity(
                id = 2,
                customerId = 5,
                title = "Chuẩn bị hồ sơ nghiệm thu dự án Khang Điền",
                description = "Rà soát lại phụ lục biên bản bàn giao và chứng chỉ bảo mật ứng dụng cư dân.",
                dueDate = timeFor(2026, 8, 31, 14, 30),
                priority = TaskPriority.HIGH.name,
                isCompleted = false,
                taskType = TaskType.SEND_PROPOSAL.name,
                location = "14:30 PM"
            ),
            TaskEntity(
                id = 3,
                customerId = 9,
                title = "Gửi email follow-up sau buổi họp với Hương Giang Logistics",
                description = "Gửi biên bản tổng kết cuộc họp và lộ trình kiểm thử tích hợp API Sandbox.",
                dueDate = timeFor(2026, 8, 31, 16, 0),
                priority = TaskPriority.MEDIUM.name,
                isCompleted = false,
                taskType = TaskType.FOLLOW_UP.name,
                location = "16:00 PM"
            ),
            TaskEntity(
                id = 4,
                customerId = 6,
                title = "Họp đánh giá hiệu quả vận hành chuỗi EcoMart tháng 8",
                description = "Tổng hợp dữ liệu doanh số qua điểm POS và gửi báo cáo cho ban giám đốc.",
                dueDate = timeFor(2026, 9, 1, 9, 30),
                priority = TaskPriority.HIGH.name,
                isCompleted = false,
                taskType = TaskType.MEETING.name,
                location = "09:30 AM"
            ),
            TaskEntity(
                id = 5,
                customerId = 13,
                title = "Khảo sát thực địa nhà máy cơ khí VinaMach",
                description = "Gặp mặt đại diện kỹ thuật để đo đạc thông số đường truyền mạng xưởng CNC.",
                dueDate = timeFor(2026, 9, 2, 14, 0),
                priority = TaskPriority.MEDIUM.name,
                isCompleted = false,
                taskType = TaskType.DEMO.name,
                location = "14:00 PM - KCN Cao"
            ),
            TaskEntity(
                id = 6,
                customerId = 2,
                title = "Bàn giao tài liệu hướng dẫn sử dụng phần mềm Hưng Phát",
                description = "Đã gửi toàn bộ tài liệu PDF và video bài giảng cho 15 kỹ sư hiện trường.",
                dueDate = timeFor(2026, 8, 25, 17, 0),
                priority = TaskPriority.LOW.name,
                isCompleted = true,
                taskType = TaskType.FOLLOW_UP.name,
                location = "Hoàn thành",
                resultRating = 5,
                resultSummary = "Khách hàng xác nhận đã tiếp nhận đầy đủ tài liệu."
            )
        )
    }

    fun getSampleQuotes(): List<QuoteItem> {
        return listOf(
            QuoteItem(
                id = 1,
                quoteNumber = "BG-20251215-001",
                title = "Gói Nâng Cấp Hệ Thống Cloud TechVN",
                amount = 180_000_000.0,
                dateStr = "15/12/2025",
                status = "Accepted",
                customerName = "Công ty CP Công Nghệ TechVN",
                customerId = 1,
                category = "Phần mềm & Bản quyền",
                notes = "Nâng cấp hạ tầng Cloud Private chuẩn ISO 27001",
                version = 1
            ),
            QuoteItem(
                id = 2,
                quoteNumber = "BG-20260110-002",
                title = "Ứng Dụng Cư Dân Khang Điền Luxury",
                amount = 210_000_000.0,
                dateStr = "10/01/2026",
                status = "Accepted",
                customerName = "Tập đoàn BĐS Khang Điền",
                customerId = 5,
                category = "Phần mềm & Bản quyền",
                notes = "Phát triển App di động iOS/Android cho cư dân",
                version = 2
            ),
            QuoteItem(
                id = 3,
                quoteNumber = "BG-20260212-003",
                title = "Hệ Thống Tích Điểm Hội Viên EcoMart",
                amount = 160_000_000.0,
                dateStr = "12/02/2026",
                status = "Accepted",
                customerName = "Chuỗi Bán Lẻ EcoMart Việt Nam",
                customerId = 6,
                category = "Dịch vụ & Bảo trì",
                notes = "Tích hợp hệ thống POS tại 25 siêu thị đợt 1",
                version = 1
            ),
            QuoteItem(
                id = 4,
                quoteNumber = "BG-20260315-004",
                title = "Thiết Kế 3D Render & CRM Showroom Casa",
                amount = 220_000_000.0,
                dateStr = "15/03/2026",
                status = "Accepted",
                customerName = "Kiến Trúc & Nội Thất Casa Luxury",
                customerId = 8,
                category = "Thiết kế & Thi công",
                notes = "Thiết kế tương tác không gian 3D thực tế ảo",
                version = 1
            ),
            QuoteItem(
                id = 5,
                quoteNumber = "BG-20260410-005",
                title = "Phần Mềm Quản Lý Thi Công Hưng Phát Giai Đoạn 2",
                amount = 310_000_000.0,
                dateStr = "10/04/2026",
                status = "Accepted",
                customerName = "Tập đoàn Xây dựng Hưng Phát",
                customerId = 2,
                category = "Phần mềm & Bản quyền",
                notes = "Gói mở rộng cho 5 dự án đô thị trọng điểm",
                version = 1
            ),
            QuoteItem(
                id = 6,
                quoteNumber = "BG-20260515-006",
                title = "Hợp Đồng Bảo Trì Toàn Diện Khang Điền 2026",
                amount = 260_000_000.0,
                dateStr = "15/05/2026",
                status = "Accepted",
                customerName = "Tập đoàn BĐS Khang Điền",
                customerId = 5,
                category = "Dịch vụ & Bảo trì",
                notes = "Bảo trì định kỳ và hỗ trợ kỹ thuật 24/7",
                version = 1
            ),
            QuoteItem(
                id = 7,
                quoteNumber = "BG-20260612-007",
                title = "Hệ Thống Quản Trị Chuỗi Cung Ứng Hương Giang",
                amount = 320_000_000.0,
                dateStr = "12/06/2026",
                status = "Accepted",
                customerName = "Hương Giang Logistics & Vận Tải Biển",
                customerId = 9,
                category = "Tư vấn giải pháp",
                notes = "Hợp đồng quản lý kho và điều phối tàu biển",
                version = 2
            ),
            QuoteItem(
                id = 8,
                quoteNumber = "BG-20260708-008",
                title = "Phần Mềm Quản Trị Tài Sản BĐS Cao Cấp",
                amount = 180_000_000.0,
                dateStr = "08/07/2026",
                status = "Accepted",
                customerName = "Vương Đình Hải",
                customerId = 15,
                category = "Phần mềm & Bản quyền",
                notes = "Hệ thống chuyên biệt theo dõi tài sản và dòng tiền",
                version = 1
            ),
            QuoteItem(
                id = 9,
                quoteNumber = "BG-20260810-009",
                title = "Smart City Khu Đô Thị Khang Điền 2026",
                amount = 350_000_000.0,
                dateStr = "10/08/2026",
                status = "Accepted",
                customerName = "Tập đoàn BĐS Khang Điền",
                customerId = 5,
                category = "Phần mềm & Bản quyền",
                notes = "Giai đoạn triển khai hệ thống IoT quản lý thông minh",
                version = 1
            ),
            QuoteItem(
                id = 10,
                quoteNumber = "BG-20260818-010",
                title = "Tích Hợp API Giao Vận Toàn Quốc Hương Giang",
                amount = 450_000_000.0,
                dateStr = "18/08/2026",
                status = "Sent",
                customerName = "Hương Giang Logistics & Vận Tải Biển",
                customerId = 9,
                category = "Dịch vụ & Bảo trì",
                notes = "Đang thẩm định điều khoản kết nối 50 đối tác giao vận",
                version = 1
            ),
            QuoteItem(
                id = 11,
                quoteNumber = "BG-20260822-011",
                title = "Giải Pháp IoT Giám Sát Nhà Máy VinaMach",
                amount = 350_000_000.0,
                dateStr = "22/08/2026",
                status = "Draft",
                customerName = "Cơ Khí Chế Tạo & Tự Động Hóa VinaMach",
                customerId = 13,
                category = "Tư vấn giải pháp",
                notes = "Bản dự thảo đính kèm danh mục 30 cảm biến đo đạc",
                version = 1
            )
        )
    }

    fun getSampleProjects(): List<ProjectProgressItem> {
        return listOf(
            ProjectProgressItem(
                id = 1,
                quoteId = 9,
                title = "Smart City Khu Đô Thị Khang Điền",
                customerName = "Tập đoàn BĐS Khang Điền",
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 40,
                steps = listOf(
                    ProjectStep(1, "Khảo sát hạ tầng mạng & IoT", StepStatus.COMPLETED, "Hoàn thành: 15/08/2026", customWeightPercent = 20),
                    ProjectStep(2, "Cài đặt máy chủ trung tâm & Cloud", StepStatus.COMPLETED, "Hoàn thành: 25/08/2026", customWeightPercent = 20),
                    ProjectStep(3, "Tích hợp phần mềm cư dân với cổng tự động", StepStatus.PENDING, "Deadline: 15/09/2026", customWeightPercent = 30),
                    ProjectStep(4, "Kiểm thử UAT & Bàn giao cư dân", StepStatus.PENDING, "Deadline: 30/09/2026", customWeightPercent = 30)
                )
            ),
            ProjectProgressItem(
                id = 2,
                quoteId = 7,
                title = "Quản Trị Chuỗi Cung Ứng Hương Giang",
                customerName = "Hương Giang Logistics",
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 70,
                steps = listOf(
                    ProjectStep(1, "Phân tích quy trình điều phối tàu", StepStatus.COMPLETED, "Hoàn thành: 20/06/2026", customWeightPercent = 25),
                    ProjectStep(2, "Xây dựng module GPS tracking container", StepStatus.COMPLETED, "Hoàn thành: 10/07/2026", customWeightPercent = 25),
                    ProjectStep(3, "Kết nối cổng thông tin cảng biển", StepStatus.COMPLETED, "Hoàn thành: 15/08/2026", customWeightPercent = 20),
                    ProjectStep(4, "Đào tạo nghiệp vụ nhân viên cảng", StepStatus.PENDING, "Deadline: 10/09/2026", customWeightPercent = 30)
                )
            ),
            ProjectProgressItem(
                id = 3,
                quoteId = 5,
                title = "Quản Lý Thi Công Dự Án Hưng Phát 2",
                customerName = "Tập đoàn Xây dựng Hưng Phát",
                statusType = ProjectStatusType.NEARING,
                progressPercent = 85,
                steps = listOf(
                    ProjectStep(1, "Cấu hình module bảng tiến độ Gantt", StepStatus.COMPLETED, "Hoàn thành: 01/05/2026"),
                    ProjectStep(2, "Đồng bộ dữ liệu định mức vật tư", StepStatus.COMPLETED, "Hoàn thành: 15/06/2026"),
                    ProjectStep(3, "Thử nghiệm báo cáo nhật ký công trường", StepStatus.WARNING, "Deadline: 05/09/2026", isHighlighted = true),
                    ProjectStep(4, "Nghiệm thu tổng thể giai đoạn 2", StepStatus.PENDING, "Dự kiến: 20/09/2026")
                )
            ),
            ProjectProgressItem(
                id = 4,
                quoteId = 4,
                title = "Showroom Không Gian 3D Casa Luxury",
                customerName = "Kiến Trúc Casa Luxury",
                statusType = ProjectStatusType.ON_TRACK,
                progressPercent = 100,
                steps = listOf(
                    ProjectStep(1, "Render 3D 10 căn hộ mẫu cao cấp", StepStatus.COMPLETED, "Hoàn thành: 10/04/2026"),
                    ProjectStep(2, "Tích hợp kính thực tế ảo VR tại Showroom", StepStatus.COMPLETED, "Hoàn thành: 25/04/2026"),
                    ProjectStep(3, "Bàn giao hệ thống quản trị bảng giá", StepStatus.COMPLETED, "Hoàn thành: 15/05/2026")
                )
            )
        )
    }

    fun getSampleEmployees(): List<EmployeeItem> {
        return listOf(
            EmployeeItem(
                id = 1,
                name = "Nguyễn Thị An",
                role = "Trưởng phòng Kinh doanh",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "NA",
                isWorking = true,
                phone = "0908 123 456",
                email = "an.nguyen@crm.vn",
                startDate = "10/02/2018", // 8.5 năm thâm niên
                baseSalary = 25_000_000.0,
                allowance = 3_500_000.0,
                kpiBonus = 7_000_000.0
            ),
            EmployeeItem(
                id = 2,
                name = "Trần Văn Bình",
                role = "Chuyên viên Kinh doanh Cao cấp",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "TB",
                isWorking = true,
                phone = "0912 345 678",
                email = "binh.tran@crm.vn",
                startDate = "15/06/2021", // 5.2 năm thâm niên (Đạt mốc 5 năm)
                baseSalary = 16_000_000.0,
                allowance = 2_000_000.0,
                kpiBonus = 5_000_000.0
            ),
            EmployeeItem(
                id = 3,
                name = "Lê Hoàng Nam",
                role = "Kỹ sư Triển khai Giải pháp",
                department = "Phòng Kỹ thuật",
                status = "Đang làm việc",
                initials = "LN",
                isWorking = true,
                phone = "0987 654 321",
                email = "nam.le@crm.vn",
                startDate = "01/03/2023", // 3.5 năm thâm niên
                baseSalary = 18_000_000.0,
                allowance = 2_000_000.0,
                kpiBonus = 3_000_000.0
            ),
            EmployeeItem(
                id = 4,
                name = "Phạm Minh Đức",
                role = "Kế toán Trưởng",
                department = "Phòng Kế toán",
                status = "Đang làm việc",
                initials = "MD",
                isWorking = true,
                phone = "0934 567 890",
                email = "duc.pham@crm.vn",
                startDate = "20/08/2017", // 9 năm thâm niên
                baseSalary = 20_000_000.0,
                allowance = 2_500_000.0,
                kpiBonus = 4_000_000.0
            ),
            EmployeeItem(
                id = 5,
                name = "Hoàng Thị Mai",
                role = "Chuyên viên Marketing & Lead Gen",
                department = "Phòng Marketing",
                status = "Đang làm việc",
                initials = "HM",
                isWorking = true,
                phone = "0978 901 234",
                email = "mai.hoang@crm.vn",
                startDate = "05/11/2022",
                baseSalary = 14_000_000.0,
                allowance = 1_500_000.0,
                kpiBonus = 3_000_000.0
            ),
            EmployeeItem(
                id = 6,
                name = "Vũ Đình Tuấn",
                role = "Chuyên viên CSKH & Onboarding",
                department = "Phòng Kinh doanh",
                status = "Đang làm việc",
                initials = "VT",
                isWorking = true,
                phone = "0945 678 901",
                email = "tuan.vu@crm.vn",
                startDate = "12/01/2020", // 6.5 năm thâm niên
                baseSalary = 15_000_000.0,
                allowance = 1_500_000.0,
                kpiBonus = 3_500_000.0
            )
        )
    }

    fun getSampleAttendance(): List<AttendanceRecord> {
        return listOf(
            AttendanceRecord(1, 1, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 2.5f, OvertimeRateType.WEEKDAY, "Chốt hợp đồng Khang Điền"),
            AttendanceRecord(2, 2, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 2.0f, OvertimeRateType.WEEKDAY, "Tăng ca xử lý báo giá Hương Giang"),
            AttendanceRecord(3, 3, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 1.5f, OvertimeRateType.WEEKDAY, "Hỗ trợ kỹ thuật On-site TechVN"),
            AttendanceRecord(4, 4, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 0.0f, OvertimeRateType.WEEKDAY, "Tổng hợp bảng kê doanh thu tháng 8"),
            AttendanceRecord(5, 5, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 1.0f, OvertimeRateType.WEEKDAY, "Chạy chiến dịch Lead Gen Q3"),
            AttendanceRecord(6, 6, "2026-08-28", AttendanceType.FULL_WORK, 8.0f, 0.0f, OvertimeRateType.WEEKDAY, "Onboarding khách hàng mới SunSmile"),
            AttendanceRecord(7, 1, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 3.0f, OvertimeRateType.WEEKDAY, "Meeting chiến lược cùng Ban Giám Đốc"),
            AttendanceRecord(8, 2, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 1.0f, OvertimeRateType.WEEKDAY, "Demo giải pháp khách hàng VinaMach"),
            AttendanceRecord(9, 3, "2026-08-27", AttendanceType.HALF_LEAVE, 4.0f, 0.0f, OvertimeRateType.WEEKDAY, "Nghỉ phép nửa buổi chiều"),
            AttendanceRecord(10, 4, "2026-08-27", AttendanceType.FULL_WORK, 8.0f, 0.0f, OvertimeRateType.WEEKDAY, "")
        )
    }
}
