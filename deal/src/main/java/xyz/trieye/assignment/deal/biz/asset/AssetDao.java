package xyz.trieye.assignment.deal.biz.asset;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AssetDao {

    @Select("select id, user_id userId, security_type securityType, security_id securityId, position from asset_t where user_id = #{userId}")
    List<AssetDTO> queryByUserId(int userId);

    @Delete("delete from asset_t where user_id = #{userId}")
    int removeOldByUserId(int userId);

    @Insert("<script>\n" +
            "  INSERT INTO asset_t (user_id, security_type, security_id, position) \n" +
            "  VALUES\n" +
            "  <foreach collection='assetList' item='item' index='index' separator=','>\n" +
            "    (#{item.userId}, #{item.securityType}, #{item.securityId}, #{item.position})\n" +
            "  </foreach>\n" +
            "</script>")
    int insertAssets(@Param("assetList") List<AssetDTO> assetList);
}
