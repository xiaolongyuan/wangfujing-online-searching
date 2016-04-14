SET FOREIGN_KEY_CHECKS = 0;

SET @MOBILE_CHANNEL = '2';
# DDD mobile渠道编码 && 是否与WEB同一渠道?

DELETE FROM online_interval_content WHERE channel = @MOBILE_CHANNEL;
INSERT INTO online_interval_content (sid, field, show_text, channel)
VALUES (2, 'currentPrice', '价格', @MOBILE_CHANNEL);
DELETE FROM online_interval_detail WHERE content_sid = 2;
INSERT INTO online_interval_detail (sid, content_sid, lower_limit, upper_limit, order_by, show_text)
VALUES (1, 2, '*', '100', 1, '0元-100元'),
  (2, 2, '100', '200', 2, '100元-200元'),
  (3, 2, '200', '300', 3, '200元-300元'),
  (4, 2, '300', '400', 4, '300元-400元'),
  (5, 2, '400', '500', 5, '400元-500元'),
  (6, 2, '500', '800', 6, '500元-800元'),
  (7, 2, '800', '1000', 7, '800元-1000元'),
  (8, 2, '1000', '2000', 8, '1000元-2000元'),
  (9, 2, '2000', '5000', 9, '2000元-5000元'),
  (10, 2, '5000', '*', 10, '5000元以上');


SET FOREIGN_KEY_CHECKS = 1;