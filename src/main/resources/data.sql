--Insert Badge
INSERT INTO Badge(badgeName, description, benefit) VALUES ('Non', N'default badge cho provider còn lại',1);
INSERT INTO Badge(badgeName, description, benefit) VALUES ('VinGiG Certified', N'Những providers đã được sàn VinGiG kiểm duyệt về chất lượng và các ràng buộc khác để đảm bảo chất lượng của các dịch vụ. Rank cao nhất.', '0.8');
INSERT INTO Badge(badgeName, description, benefit) VALUES ('Best Provider', N'Dành cho 1 provider có lượt booking cao nhất của 1 tháng. Badge kéo dài trong 1 tháng', '0.8');
INSERT INTO Badge(badgeName, description, benefit) VALUES ('Top-rated Provider', N'Dành cho những provider nào có trên 80 booking/ tháng và rating > 4.5 sao. Badge kéo dài trong 1 tháng', '0.9');
INSERT INTO Badge(badgeName, description, benefit) VALUES ('Preferred Provider', N'Dành cho những provider nào có trên 60 booking/ tháng và rating > 4 sao. Badge kéo dài trong 1 tháng', '0.95');
INSERT INTO Badge(badgeName, description, benefit) VALUES ('Promising Provider', N'Dành cho những provider mới tham gia VinGiG. Badge kéo dài trong 1 tuần.', '0.9');

--Insert Plan
INSERT INTO [Plan] (description, duration, price) VALUES (N'Gói Tháng', '30', '50000');
INSERT INTO [Plan] (description, duration, price) VALUES (N'Gói Qúy', '90', '140000');
INSERT INTO [Plan] (description, duration, price) VALUES (N'Gói Bán Niên', '182', '250000');
INSERT INTO [Plan] (description, duration, price) VALUES (N'Gói Thường Niên', '365', '480000');

--Insert ServiceCategory
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Máy Lạnh', N'Kiểm tra máy lạnh/Vệ sinh màng lọc/Sửa chữa máy lạnh/Bơm gas máy lạnh');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Bào trì & Sửa chữa nhà', N'Sơn lại tường, Thay pin cửa, Thông cống bồn cầu, Thay mặt bồn rửa, Lắp đặt hệ thống nước/Thay kiếng');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Việc nhà', N'Combo việc nhà: rửa chén, quét dọn, lau nhà, vệ sinh toilet');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Chăm sóc tại gia', N'Giữ trẻ/chăm bệnh người già');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Theo dõi sức khỏe', N'Truyền dịch nước biển/ vệ sinh vết thương/ thăm khám bệnh tại gia');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Chợ/Bếp', N'Đi chợ/ Nấu cơm');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Giặt giũ', N'Giặt xả quần áo/ Giặt rèm cửa');
INSERT INTO ServiceCategory (categoryName, description) VALUES (N'Vệ sinh thiết bị', N'Vệ sinh máy giặt/ Vệ sinh dàn máy tính');

--Insert Service
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('1', N'Kiểm tra máy lạnh', N'kiểm tra máy lạnh còn chạy được không,bụi bẩn như thế nào, gas, đầu nóng đầu lạnh', N'máy', '20000', '50000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('1', N'Vệ sinh máy lạnh', N'tiến hành tháo giỡ giàn áo, vệ sinh đầu lạnh và đầu nóng', N'máy', '120000', '200000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('1', N'Sửa chữa máy lạnh', N'Sửa chữa các vấn đề liên quan đến máy lạnh', N'máy', '250000', '450000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('1', N'Bơm gas máy lạnh', N'tiến hành đo lượng ga của máy lạnh và bơm đầy', N'máy', '200000', '300000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Sơn lại tường', N'trao đổi với anh/chị về diện tích phòng, trao đổi với nhau sau đó hẹn ngày sơn tường.', N'm2', '25000', '45000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Thay pin cửa', N'trao đổi với anh/chị về loại cửa nhà dùng và đem pin thích hợp nhất đến thay', N'cửa', '20000', '60000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Thông cống bồn cầu', N'với công cụ vạn năng, chúng tôi thông bất kì bồn nào, dùng thuốc hoặc sức', N'chiếc', '200000', '400000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Thay mặt bồn rửa', N'trao đổi với anh/chị về loại bồn mặt', N'chiếc', '700000', '1500000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Dịch vụ thay bóng đèn ', N'thông báo số lượng cần thay và tiến hành thay', N'bóng', '400000', '600000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('2', N'Thay kiếng cửa', N'tiến hành sơ thẩm, đến nhà xem xét và đưa ra quyết định', N'm2', '350000', '500000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('3', N'Combo việc nhà: rửa chén, quét dọn, lau nhà, vệ sinh toilet', N'trao đối với anh chị với loại phòng ngủ để ước lượng số giờ làm việc', N'tiếng', '50000', '100000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('4', N'Giữ trẻ', N'gọi cho anh/chị quyết định ngày giờ trông', N'tiếng', '40000', '100000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('4', N'Chăm bệnh người già', N'trao đổi với anh chị về số giờ chăm và làm', N'tiếng', '40000', '100000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('5', N'Truyền dịch', N'Truyền dịch theo đơn hoặc theo yêu cầu bệnh nhân', N'chai', '100000', '200000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('5', N'Vệ sinh vết thương', N'tiến hành sát trùng, thay băng bông và kiểm tra vết thương', N'lần', '50000', '80000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('5', N'Thăm khám bệnh tại gia', N'hẹn giờ với anh chị  đi đến trao đổi bệnh tình hoặc là qua điện thoại', N'lần', '100000', '170000', '10000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('6', N'Đi chợ', N'lấy order của khách hàng qua tin nhắn hoặc là hội thoại sau đó đi và đem về cho quý khách', N'lần', '40000', '80000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('7', N'Giặt xả quần áo', N'đến nơi lấy đồ về và tiến hành cân kí sau liên lạc với quý khách số cân và giao dịch', N'Kg', '5000', '10000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('7', N'Giặt rèm cửa', N'đến nơi và tháo rèm cửa đem về giặt, phân loại chất liệu rèm mà tiến hành giặt và sau khi khô sẽ tiến hành lắp lại cho quý khách', N'Kg', '30000', '50000', '5000');
INSERT INTO Service (categoryID, serviceName, description, unit, priceMin, priceMax, fee) VALUES ('8', N'Vệ sinh máy giặt', N'vệ sinh lồng giặt, kiểm tra máy giặt còn ổn định không', N'máy', '150000', '250000', '5000');

--Insert Building
INSERT INTO Building (buildingName, note) VALUES ('S1.01', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S1.02', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S1.03', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S1.05', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S1.06', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S1.07', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S2.01', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S2.02', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S2.03', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S2.05', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S3.01', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S3.02', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S3.03', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S3.05', 'Khu Rainbow');
INSERT INTO Building (buildingName, note) VALUES ('S5.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S5.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S5.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S6.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S6.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S6.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S6.05', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S6.06', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S7.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S7.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S7.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S7.05', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S8.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S8.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S8.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S9.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S9.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S9.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.01', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.02', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.03', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.05', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.06', 'Khu Origami');
INSERT INTO Building (buildingName, note) VALUES ('S10.07', 'Khu Origami');
