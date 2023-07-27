CREATE DATABASE VinGiG
USE VinGiG
GO;

create table Badge (badgeID int identity not null, active BIT DEFAULT 1 not null, badgeName nvarchar(255) not null, benefit double precision, description nvarchar(255) not null, primary key (badgeID));
create table Booking (bookingID bigint identity not null, apartment nvarchar(255) not null, customersRating int, customersReview NVARCHAR(300) NULL, date datetime2, providersRating int, providersReview NVARCHAR(300) NULL, status int not null, total bigint, unitPrice bigint not null, buildingID int not null, customerID bigint not null, proServiceID bigint not null, primary key (bookingID));
create table BookingFee (bookingFeeID bigint identity not null, amount bigint not null, date datetime2 not null, bookingID bigint not null, primary key (bookingFeeID));
create table BookingMessage (messageID bigint identity not null, content NVARCHAR(700) not null, sendBy bit not null, time datetime2, bookingID bigint not null, primary key (messageID));
create table Building (buildingID int identity not null, active BIT DEFAULT 1 not null, buildingName nvarchar(255) not null, note nvarchar(255), primary key (buildingID));
create table Count (countID bigint identity not null, active BIT DEFAULT 1 not null, count int not null, providerID bigint not null, primary key (countID));
create table Customer (customerID bigint identity not null, active BIT DEFAULT 1 not null, address nvarchar(255) not null, avatar NVARCHAR(300) DEFAULT 'https://media.istockphoto.com/id/1311497219/vector/stylish-avatar-in-black-in-a-yellow-circle-profile-template-for-websites-applications-social.jpg?s=612x612&w=0&k=20&c=SzDRaxQ_ckdqqPbjFqw23uTgj6bbu3x4G66O7j08o6U=', createDate datetime2 not null, dob datetime2, email nvarchar(255) not null, fullName nvarchar(255) not null, gender bit not null, password nvarchar(255) not null, phone nvarchar(255) not null, rating double precision, role NVARCHAR(10) DEFAULT 'customer' not null, username nvarchar(255) not null, buildingID int not null, primary key (customerID));
create table Deposit (depositID bigint identity not null, amount bigint not null, date datetime2 not null, method NVARCHAR(2000) not null, success bit not null, providerID bigint not null, primary key (depositID));
create table GiGService (serviceID int identity not null, active BIT DEFAULT 1 not null, description NVARCHAR(300) not null, fee bigint not null, priceMax bigint not null, priceMin bigint not null, serviceName NVARCHAR(100) not null, unit NVARCHAR(20) not null, categoryID int not null, primary key (serviceID));
create table Image (imageID bigint identity not null, link NVARCHAR(300) not null, proServiceID bigint not null, primary key (imageID));
create table Provider (providerID bigint identity not null, active BIT DEFAULT 1 not null, address nvarchar(255) not null, avatar NVARCHAR(300) DEFAULT 'https://media.istockphoto.com/id/1311497219/vector/stylish-avatar-in-black-in-a-yellow-circle-profile-template-for-websites-applications-social.jpg?s=612x612&w=0&k=20&c=SzDRaxQ_ckdqqPbjFqw23uTgj6bbu3x4G66O7j08o6U=', createDate datetime2 not null, email nvarchar(255) not null, fullName nvarchar(255) not null, gender bit not null, password nvarchar(255) not null, phone nvarchar(255) not null, rating double precision, role NVARCHAR(10) DEFAULT 'provider' not null, username nvarchar(255) not null, badgeID int not null, buildingID int not null, primary key (providerID));
create table ProviderService (proServiceID bigint identity not null, active BIT DEFAULT 1 not null, availability bit not null, bookingNo INT DEFAULT 0, description NVARCHAR(2500) not null, rating double precision, unitPrice bigint not null, visible BIT DEFAULT 1 not null, providerID bigint not null, serviceID int, primary key (proServiceID));
create table ServiceCategory (categoryID int identity not null, active BIT DEFAULT 1 not null, categoryName nvarchar(255) not null, description nvarchar(255) not null, primary key (categoryID));
create table SubscriptionFee (subID bigint identity not null, amount bigint not null, date datetime2 not null, planID int not null, providerID bigint not null, primary key (subID));
create table SubscriptionPlan (planID int identity not null, active BIT DEFAULT 1 not null, description nvarchar(255) not null, duration int not null, price bigint not null, primary key (planID));
create table Transction (transactionID bigint identity not null, amount bigint not null, date datetime2 not null, bookingFeeID bigint, depositID bigint, subID bigint, walletID bigint not null, primary key (transactionID));
create table Wallet (walletID bigint identity not null, active BIT DEFAULT 1 not null, balance BIGINT DEFAULT 0 not null, createDate datetime2 not null, providerID bigint not null, primary key (walletID));
alter table Customer add constraint UK_mufchskagt7e1w4ksmt9lum5l unique (username);
alter table Provider add constraint UK_mkkvbrmm0tlnfn5xann6n0bpy unique (username);
alter table Booking add constraint FKdpgrkjth16aawhcprlq2tbbrw foreign key (buildingID) references Building;
alter table Booking add constraint FKoaegj0j83e2qjfah70g6s9ktt foreign key (customerID) references Customer;
alter table Booking add constraint FK2e1frw29sk08c8tft24h2udx3 foreign key (proServiceID) references ProviderService;
alter table BookingFee add constraint FKn53h7xvuu30sbxskpgb06op47 foreign key (bookingID) references Booking;
alter table BookingMessage add constraint FKsvd89il5acq6he8yskwtuy8j7 foreign key (bookingID) references Booking;
alter table Count add constraint FKsjuw7k0549xrrx37gxfumypm4 foreign key (providerID) references Provider;
alter table Customer add constraint FKn055k3r3llr1a2fyb4vk0frt4 foreign key (buildingID) references Building;
alter table Deposit add constraint FK57u7phwg03niu3ew27ym0mqps foreign key (providerID) references Provider;
alter table GiGService add constraint FKheqhcngdc0n4cn0bym40l59kl foreign key (categoryID) references ServiceCategory;
alter table Image add constraint FKk94fo1h8hp2hkjv25kirn9b3b foreign key (proServiceID) references ProviderService;
alter table Provider add constraint FKkkoemin16x63xxh4sm6x55wl6 foreign key (badgeID) references Badge;
alter table Provider add constraint FKst2f9hlswqf81lltirecnxccx foreign key (buildingID) references Building;
alter table ProviderService add constraint FKtdjnsp079i1def0d2nqk21h1p foreign key (providerID) references Provider;
alter table ProviderService add constraint FKax39mr9lorr47fjaevv4yff7i foreign key (serviceID) references GiGService;
alter table SubscriptionFee add constraint FK64rinyj0d62cntde7xu11kxby foreign key (planID) references SubscriptionPlan;
alter table SubscriptionFee add constraint FKtj7yl4wjhqjj155ri7tufd8rr foreign key (providerID) references Provider;
alter table Transction add constraint FK8psuewjvnp5mcvf9uvmw7bfdk foreign key (bookingFeeID) references BookingFee;
alter table Transction add constraint FKm45wdqobsf6k6jk4sw3nivo37 foreign key (depositID) references Deposit;
alter table Transction add constraint FKnm2knt5srfd59xxhllvkrsrvn foreign key (subID) references SubscriptionFee;
alter table Transction add constraint FKmvuyd840j24gysd4te0hygh2d foreign key (walletID) references Wallet;
alter table Wallet add constraint FKnohmnw1jj0ngjn4t3dv6dbe1v foreign key (providerID) references Provider;
