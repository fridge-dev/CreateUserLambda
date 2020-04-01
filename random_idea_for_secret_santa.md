UserData
- (Hash) UserId - String

FamilyData
- (Hash) FamilyId - String

FamilyMembershipData
- (Hash, GSI Range) UserId - String
- (Range, GSI Hash) FamilyId - String

CommentData
- (Hash) ParentTopicId - String
- (Range) CreationTimeMillis - long




GET /profile
- load home page
- viewing UserId in cookie

GET /family/{FamilyId}
- load family page
- viewing UserId in cookie

GET /family/{FamilyId}/member/{MemberId}
- load a topic
- should support DisplayName as well?
- how to handle self-viewing?
- viewing UserId in cookie

POST /family/{FamilyId}/member/{MemberId}
- create/update self topic

POST /family/{FamilyId}/member/{MemberId}/comment
- create/update comment

