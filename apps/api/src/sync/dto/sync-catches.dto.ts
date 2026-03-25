import { Type } from 'class-transformer';
import { IsArray, ValidateNested } from 'class-validator';
import { CreateCatchDto } from '../../catches/dto/create-catch.dto';

export class SyncCatchesDto {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreateCatchDto)
  items!: CreateCatchDto[];
}
