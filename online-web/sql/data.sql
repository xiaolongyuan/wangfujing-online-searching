SET FOREIGN_KEY_CHECKS = 0;

SET @WEB_CHANNEL = '0';

DELETE FROM online_sort_rule WHERE channel = @WEB_CHANNEL;
INSERT INTO online_sort_rule
(sid, channel, order_field, channel_default, show_text, show_order, default_order_by, other_order_by)
VALUES (1, @WEB_CHANNEL, 'score', true, '综合', 0, 'desc', 'desc'),
  (2, @WEB_CHANNEL, 'upTime', false, '最新上架', 1, 'desc', 'asc'),
  (3, @WEB_CHANNEL, 'spuSale', false, '销量', 2, 'desc', 'desc'),
  (4, @WEB_CHANNEL, 'currentPrice', false, '价格', 3, 'asc', 'desc'),
  (5, @WEB_CHANNEL, 'discountRate', false, '折扣', 4, 'asc', 'desc');

DELETE FROM online_interval_content WHERE channel = @WEB_CHANNEL;
INSERT INTO online_interval_content (sid, field, show_text, channel, selected)
VALUES (1, 'currentPrice', '价格', @WEB_CHANNEL, TRUE);
DELETE FROM online_interval_detail WHERE content_sid = 1;
INSERT INTO online_interval_detail (sid, content_sid, lower_limit, upper_limit, order_by, show_text)
VALUES (1, 1, '*', '100', 1, '0元-100元'),
  (2, 1, '100', '200', 2, '100元-200元'),
  (3, 1, '200', '300', 3, '200元-300元'),
  (4, 1, '300', '400', 4, '300元-400元'),
  (5, 1, '400', '500', 5, '400元-500元'),
  (6, 1, '500', '800', 6, '500元-800元'),
  (7, 1, '800', '1000', 7, '800元-1000元'),
  (8, 1, '1000', '2000', 8, '1000元-2000元'),
  (9, 1, '2000', '5000', 9, '2000元-5000元'),
  (10, 1, '5000', '*', 10, '5000元以上');

SET FOREIGN_KEY_CHECKS = 1;